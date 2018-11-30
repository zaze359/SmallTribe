package com.zaze.router

import android.app.Application
import android.content.Context
import com.zaze.router.interceptor.IInterceptor

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-26 - 17:05
 */
object ZRouter {

    private var hasInit = false

    @JvmStatic
    fun init(application: Application, rootPackageName: String) {
        if (!hasInit) {
            hasInit = _ZRouter.init(application, rootPackageName)
        }
    }

    fun registerInterceptor(interceptor: IInterceptor): ZRouter {
        _ZRouter.registerInterceptor(interceptor)
        return this
    }

    @JvmStatic
    fun openDebug() {
        _ZRouter.openDebug()
    }

    @JvmStatic
    fun debuggable(): Boolean {
        return _ZRouter.debuggable()
    }

    // --------------------------------------------------

    fun build(path: String): Postcard {
        return _ZRouter.build(path)
    }

    fun <T> getModel(className: Class<T>): T {
        return _ZRouter.getModel(className)
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
