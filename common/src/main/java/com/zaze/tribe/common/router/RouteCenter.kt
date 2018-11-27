package com.zaze.tribe.common.router

import android.content.Context
import com.zaze.tribe.common.router.interceptor.IInterceptor
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
        val startTime = System.currentTimeMillis()
        // TODO 比对是否存在该版本到信息, 存在则直接读取
        RouteLoader(context).loadInto(routes)
    }

    fun registerInterceptor(interceptor: IInterceptor) {
        interceptors.add(interceptor)
    }

    fun clear() {
        routes.clear()
    }
}