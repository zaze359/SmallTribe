package com.zaze.tribe

import android.app.Application
import com.zaze.tribe.common.base.BaseApplication
import com.zaze.tribe.common.util.StartupStore
import com.zaze.tribe.common.util.StartupTask
import com.zaze.utils.log.ZLog
import dagger.hilt.android.HiltAndroidApp

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-07-05 - 23:25
 */
@HiltAndroidApp
class App : BaseApplication() {

    override fun initStartupTask(application: Application, startupStore: StartupStore) {
        super.initStartupTask(application, startupStore)
        startupStore.push(object : StartupTask("ZRouter") {
            override fun doTask() {
                ZLog.setNeedStack(false)
//                ZRouter.openDebug()
//                ZRouter.init(application, "com.zaze.tribe")
            }
        })
    }
}
