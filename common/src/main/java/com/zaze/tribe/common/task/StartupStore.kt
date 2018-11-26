package com.zaze.tribe.common.task

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
        if (!tasks.isEmpty()) {
            CountDownLatch(tasks.size).let { countDownLatch ->
                val executor = Executors.newFixedThreadPool(tasks.size)
                tasks.forEach { it ->
                    it.countDownLatch = countDownLatch
                    executor.execute(it)
                }
                countDownLatch.await()
            }
        }
    }
}