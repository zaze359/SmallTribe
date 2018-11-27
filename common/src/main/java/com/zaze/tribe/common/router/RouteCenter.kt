package com.zaze.tribe.common.router

import android.content.Context
import com.zaze.tribe.common.BuildConfig
import com.zaze.tribe.common.router.RouteCenter.interceptors
import com.zaze.tribe.common.router.RouteCenter.routes
import com.zaze.tribe.common.router.interceptor.IInterceptor
import com.zaze.tribe.common.router.loader.ClassUtils
import com.zaze.tribe.common.router.loader.RouteLoader

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-11-27 - 00:23
 */
internal object RouteCenter {
    @JvmField
    val routes = HashMap<String, RouteMeta>()

    @JvmField
    val interceptors = ArrayList<IInterceptor>()

    fun init(context: Context) {
        // TODO 比对是否存在该版本到信息, 存在则直接读取
        if (ZRouter.debuggable() || RouteCache.isNewVersion(context)) {
            RouteLoader(context).loadInto(routes)
            val classNames = ClassUtils.getFileNameByPackageName(context, context.packageName)
            RouteCache.updateAllClassNames(context, classNames)
            RouteCache.updateVersion(context)
        }
    }

    fun registerInterceptor(interceptor: IInterceptor) {
        interceptors.add(interceptor)
    }

    fun clear() {
        routes.clear()
    }
}