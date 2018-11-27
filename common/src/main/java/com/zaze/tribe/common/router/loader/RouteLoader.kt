package com.zaze.tribe.common.router.loader

import android.content.Context
import android.content.pm.PackageManager
import com.zaze.tribe.common.router.RouteMeta
import com.zaze.tribe.common.router.RouteType
import com.zaze.tribe.common.router.anno.Router

/**
 * Description : 加载所有Router
 *
 * @author : ZAZE
 * @version : 2018-11-27 - 10:02
 */
class RouteLoader(private val context: Context) {

    fun loadInto(map: MutableMap<String, RouteMeta>) {
        context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES).let { it ->
            it.activities?.forEach { act ->
                val actClass = Class.forName(act.name)
                actClass.getAnnotation(Router::class.java)?.let { router ->
                    map[router.path] = RouteMeta().apply {
                        path = router.path
                        target = actClass
                        type = RouteType.ACTIVITY
                    }
                }
            }
        }
    }
}
