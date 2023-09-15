package com.zaze.tribe.common.base

import android.app.Application
import com.zaze.tribe.common.util.StartupStore
import com.zaze.tribe.common.util.StartupTask
import com.zaze.utils.DisplayUtil

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
                .push(object : StartupTask("LeakCanary, Default init") {
                    override fun doTask() {
                        DisplayUtil.init(INSTANCE)
                    }
                })
        initStartupTask(INSTANCE, startupStore)
        startupStore.execute()
    }

    open fun initStartupTask(application: Application, startupStore: StartupStore) {
    }
}
