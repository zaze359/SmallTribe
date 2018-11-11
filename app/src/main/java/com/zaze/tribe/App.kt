package com.zaze.tribe

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import com.zaze.utils.ZDisplayUtil
import com.zaze.utils.ZSharedPrefUtil

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-07-05 - 23:25
 */
class App : Application() {
    companion object {
        lateinit var INSTANCE: App
    }

    override fun onCreate() {
        super.onCreate()
        LeakCanary.install(this)
        INSTANCE = this
        ZDisplayUtil.init(this)
    }

}
