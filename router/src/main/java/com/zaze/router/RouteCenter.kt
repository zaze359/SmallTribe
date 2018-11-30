package com.zaze.router

import android.content.Context
import android.util.Log
import com.zaze.router.interceptor.IInterceptor
import com.zaze.router.loader.ClassUtils.loadInto
import com.zaze.router.loader.InterceptorLoader
import com.zaze.router.loader.ModelLoader
import com.zaze.router.loader.RouterLoader
import com.zaze.router.task.StartupStore
import java.util.*

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-11-27 - 00:23
 */
internal object RouteCenter {

    @JvmField
    val routes = HashMap<String, RouteMeta>()

    @JvmStatic
    val models = HashMap<Class<*>, String>()

    @JvmField
    val interceptors = ArrayList<IInterceptor>()

    fun init(context: Context, rootPackageName: String) {
        try {
            if (ZRouter.debuggable() || RouteCache.isNewVersion(context)) {
                val classNames = HashSet<String>()
                val startupStore = StartupStore.build()
//                startupStore.push(object : StartupTask() {
//                    override fun doTask() {
//                        RouterLoader(context).loadInto(routes)
//                    }
//                })
                loadInto(context, rootPackageName, classNames, startupStore)
                startupStore.execute()
                Log.d("RouteCenter", "Filter " + classNames.size + " classes by rootPackageName <" + rootPackageName + ">")
                if (!classNames.isEmpty()) {
                    RouteCache.updateAllClassNames(context, classNames)
                }
                RouteCache.updateVersion(context)
            } else {
                RouteCache.getAllClassNames(context)?.let {
                    it.forEach { className ->
                        loadClass(context, rootPackageName, className)
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun loadClass(context: Context, rootPackageName: String, className: String): Boolean {
        Class.forName(className).let {
            if (RouterLoader(context).loadInto(it, routes)) {
                return true
            }
            if (ModelLoader(rootPackageName).loadInto(it, models)) {
                return true
            }
            return InterceptorLoader().loadInto(it, interceptors)
        }
    }

    @Deprecated("")
    fun registerInterceptor(interceptor: IInterceptor) {
        interceptors.add(interceptor)
    }

    fun clear() {
        routes.clear()
    }
}