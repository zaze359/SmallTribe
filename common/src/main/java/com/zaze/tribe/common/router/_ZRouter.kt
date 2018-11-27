package com.zaze.tribe.common.router

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.zaze.tribe.common.router.interceptor.IInterceptor

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-26 - 17:05
 */
internal object _ZRouter {
    private lateinit var application: Application

    private lateinit var mHandler: Handler

    @Synchronized
    fun init(application: Application): Boolean {
        RouteCenter.init(application)
        mHandler = Handler(Looper.getMainLooper())
        this.application = application
        return true
    }

    fun build(): RouteMeta {
        return RouteMeta()
    }

    fun registerInterceptor(interceptor: IInterceptor) {
        RouteCenter.registerInterceptor(interceptor)
    }

    /**
     * navigate
     * [context] context
     * [postcard] postcard
     * [requestCode] requestCode
     * [callback] callback
     */
    fun navigate(context: Context? = null, postcard: Postcard, requestCode: Int = 0, callback: NavigationCallback? = null): Any? {
        // TODO 拦截器
        val routeMeta = RouteCenter.routes[postcard.path]
        routeMeta?.let {

        }
        return _navigate(context, postcard, requestCode, callback)
    }

    fun _navigate(context: Context? = null, postcard: Postcard, requestCode: Int = 0, callback: NavigationCallback? = null): Any? {
        val curContext = (context ?: application.applicationContext)
        when (postcard.type) {
            RouteType.ACTIVITY -> {
                val intent = Intent(curContext, postcard.target)
                postcard.extras?.let {
                    intent.putExtras(it)
                }
                //
                if (postcard.flags != -1) {
                    intent.flags = postcard.flags
                } else if (curContext !is Activity) {
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                //
                if (!TextUtils.isEmpty(postcard.action)) {
                    intent.action = postcard.action
                }
                runInMainThread(Runnable { curContext.startActivity(intent) })
            }
            RouteType.FRAGMENT_X -> {
                postcard.target?.let {
                    try {
                        val instance = it.getConstructor().newInstance()
                        if (instance is Fragment) {
                            instance.arguments = postcard.extras
                        }
                        return instance
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            else -> {

            }
        }
        return null
    }


    /**
     * runInMainThread
     * [runnable] runnable
     */
    private fun runInMainThread(runnable: Runnable) {
        if (Looper.getMainLooper().thread != Thread.currentThread()) {
            mHandler.post(runnable)
        } else {
            runnable.run()
        }
    }

    private fun startActiity(context: Context, intent: Intent, requestCode: Int, postcard: Postcard, callback: NavigationCallback?) {
        if (requestCode >= 0) {  // Need start for result
            if (context is Activity) {
                ActivityCompat.startActivityForResult(context, intent, requestCode, postcard.options)
            } else {
                Log.e("", "Must use [navigation(activity, ...)] to support [startActivityForResult]")
            }
        } else {
            ActivityCompat.startActivity(context, intent, postcard.options)
        }
//        callback?
    }
}
