package com.zaze.tribe.common

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import com.zaze.tribe.common.task.StartupStore
import com.zaze.tribe.common.task.StartupTask
import com.zaze.utils.ZDisplayUtil

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-07-05 - 23:25
 */
open class BaseApplication : Application() {
    companion object {
        lateinit var INSTANCE: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        val startupStore = StartupStore.build()
                .push(object : StartupTask() {
                    override fun doTask() {
                        LeakCanary.install(INSTANCE)
                    }
                }).push(object : StartupTask() {
                    override fun doTask() {
                        ZDisplayUtil.init(INSTANCE)
                    }
                })
        initStartupTask(startupStore)
        startupStore.execute()
    }

    fun initStartupTask(startupStore: StartupStore) {
    }

}
