package com.zaze.router.task

import android.util.Log
import com.zaze.router.thread.DefaultPoolExecutor
import java.util.*
import java.util.concurrent.CountDownLatch

/**
 * Description : 启动任务库
 * 并发完成初始化任务
 *
 * @author : ZAZE
 * @version : 2018-11-26 - 22:30
 */
class StartupStore private constructor() {

    private val tasks = ArrayList<StartupTask>()
    private var isRunning = false
    private val executor = DefaultPoolExecutor.getInstance()
    private var debugLog = false

    fun openLogger(): StartupStore {
        debugLog = true
        return this
    }

    companion object {
        @JvmStatic
        fun build(): StartupStore {
            return StartupStore()
        }
    }

    fun push(task: StartupTask): StartupStore {
        tasks.add(task)
        return this
    }

    fun execute() {
        if (!isRunning) {
            isRunning = true
            val startTime = System.currentTimeMillis()
            if (tasks.isNotEmpty()) {
                CountDownLatch(tasks.size).let { countDownLatch ->
                    tasks.forEach {
                        it.countDownLatch = countDownLatch
                        it.debugLog = debugLog
                        executor.execute(it)
                    }
                    countDownLatch.await()
                }
                tasks.clear()
            }
            if (debugLog) {
                Log.i("StartupStore", "该批次 总耗时 : ${System.currentTimeMillis() - startTime}")
            }
            isRunning = false
        }
    }
}