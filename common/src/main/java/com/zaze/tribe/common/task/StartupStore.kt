package com.zaze.tribe.common.task

import android.util.Log
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

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
    private val executor = Executors.newFixedThreadPool(5)

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
            if (!tasks.isEmpty()) {
                CountDownLatch(tasks.size).let { countDownLatch ->
                    tasks.forEach { it ->
                        it.countDownLatch = countDownLatch
                        executor.execute(it)
                    }
                    countDownLatch.await()
                }
                tasks.clear()
            }
            Log.i("StartupStore", "耗时 : ${System.currentTimeMillis() - startTime}")
            isRunning = false
        }
    }
}