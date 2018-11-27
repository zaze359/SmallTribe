package com.zaze.tribe.common.router

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-11-26 - 23:47
 */
open class RouteMeta {
    /**
     * 路径 /xx/xxx
     */
    var path: String = ""
    /**
     * 目标
     */
    var target: Class<*>? = null

    /**
     * 路由类型
     */
    var type: RouteType? = null

    fun path(path: String): RouteMeta {
        this.path = path
        return this
    }

    fun target(target: Class<*>): RouteMeta {
        this.target = target
        return this
    }
}