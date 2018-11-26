package com.zaze.tribe.common.task

import android.util.Log
import java.lang.Exception
import java.util.concurrent.CountDownLatch

/**
 * Description : 启动任务
 *
 * @author : ZAZE
 */
abstract class StartupTask(private val taskName: String? = null) : Runnable {

    var isStartup = false
    var countDownLatch: CountDownLatch? = null

    override fun run() {
        isStartup = try {
            doTask()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            countDownLatch?.countDown()
        }
        Log.i("StartupTask", "${(taskName ?: this.hashCode().toString())} Startup : $isStartup")
    }

    abstract fun doTask()
}