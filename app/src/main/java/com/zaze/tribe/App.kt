package com.zaze.tribe

import android.app.Application
import com.zaze.tribe.common.BaseApplication
import com.zaze.tribe.common.router.ZRouter
import com.zaze.tribe.common.task.StartupStore
import com.zaze.tribe.common.task.StartupTask

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
                ZRouter.init(application)
            }
        })
    }
}
