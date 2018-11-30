package com.zaze.router

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.zaze.router.interceptor.IInterceptor
import com.zaze.router.proxy.ModelProxy

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-26 - 17:05
 */
internal object _ZRouter {
    private lateinit var application: Application

    private lateinit var mHandler: Handler
    private var debuggable = false

    @Synchronized
    fun init(application: Application, rootPackageName: String): Boolean {
        mHandler = Handler(Looper.getMainLooper())
        this.application = application
        try {
            RouteCenter.init(application, rootPackageName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    fun build(path: String): Postcard {
        return Postcard(path)
    }

    fun registerInterceptor(interceptor: IInterceptor) {
        RouteCenter.registerInterceptor(interceptor)
    }

    // ------------------------------------------------------

    fun openDebug() {
        debuggable = true
    }

    fun debuggable(): Boolean {
        return debuggable
    }

    // ------------------------------------------------------

    /**
     * navigate
     * [context] context
     * [postcard] postcard
     * [requestCode] requestCode
     * [callback] callback
     */
    fun navigate(context: Context? = null, postcard: Postcard, requestCode: Int = 0, callback: NavigationCallback? = null): Any? {
        // TODO 拦截器
        RouteCenter.routes[postcard.path]?.let {
            postcard.target = it.target
            postcard.type = it.type
        }
//                ?: postcard.let {
//            postcard.target = NotFountActivity::class.java
//            postcard.type = RouteType.ACTIVITY
//        }
        return _navigate(context, postcard, requestCode, callback)
    }

    private fun _navigate(context: Context? = null, postcard: Postcard, requestCode: Int = 0, callback: NavigationCallback? = null): Any? {
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
            RouteType.FRAGMENT_V4 -> {
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
                runInMainThread(Runnable {
                    Toast.makeText(curContext, "找不到指定path: ${postcard.path}", Toast.LENGTH_LONG).show()
                })
            }
        }
        return null
    }


    // --------------------------------------------------
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

    // --------------------------------------------------

    fun <T> getModel(cls: Class<T>): T {
        val modelImpl = RouteCenter.models[cls]
        try {
            val instance = modelImpl?.run {
                Class.forName(this).newInstance() as T
            }
            instance?.let {
                return it
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        runInMainThread(Runnable {
            modelImpl?.let {
                Toast.makeText(application, "找不到指定ModelImpl: $it", Toast.LENGTH_LONG).show()
            } ?: Toast.makeText(application, "Model未定义注解: $cls", Toast.LENGTH_LONG).show()
        })
        return ModelProxy.getModel(cls)

    }


}
