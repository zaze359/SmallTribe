package com.zaze.tribe

import android.app.Application
import com.zaze.router.ZRouter
import com.zaze.router.task.StartupStore
import com.zaze.router.task.StartupTask
import com.zaze.tribe.common.BaseApplication

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-07-05 - 23:25
 */
class App : BaseApplication() {

    override fun initStartupTask(application: Application, startupStore: StartupStore) {
        super.initStartupTask(application, startupStore)
        startupStore.push(object : StartupTask("ZRouter") {
            override fun doTask() {
//                ZRouter.openDebug()
                ZRouter.init(application, "com.zaze.tribe")
            }
        })
    }
}
