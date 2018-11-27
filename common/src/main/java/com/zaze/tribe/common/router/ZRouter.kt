package com.zaze.tribe.common.router

import android.app.Application
import android.content.Context
import com.zaze.tribe.common.router.interceptor.IInterceptor

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-26 - 17:05
 */
object ZRouter {

    private var hasInit = false

    fun init(application: Application) {
        if (!hasInit) {
            hasInit = _ZRouter.init(application)
        }
    }

    fun registerInterceptor(interceptor: IInterceptor): ZRouter {
        _ZRouter.registerInterceptor(interceptor)
        return this
    }

    fun openDebug() {
        _ZRouter.openDebug()
    }

    fun debuggable(): Boolean {
        return _ZRouter.debuggable()
    }

    // --------------------------------------------------

    fun build(path : String): Postcard {
        return _ZRouter.build(path)
    }

    /**
     * navigate
     * [context] context
     * [postcard] postcard
     * [requestCode] requestCode
     * [callback] callback
     */
    internal fun navigate(context: Context? = null, postcard: Postcard, requestCode: Int = 0, callback: NavigationCallback? = null): Any? {
        return _ZRouter.navigate(context, postcard, requestCode, callback)
    }
}
