package com.zaze.router.loader

import android.content.Context
import android.content.pm.PackageManager
import com.zaze.router.RouteMeta
import com.zaze.router.RouteType
import com.zaze.router.anno.Router

/**
 * Description : 加载所有Router
 *
 * @author : ZAZE
 * @version : 2018-11-27 - 10:02
 */
class RouterLoader(private val context: Context) {

    @Deprecated("")
    fun loadInto(map: MutableMap<String, RouteMeta>) {
        context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES).let { it ->
            it.activities?.forEach { act ->
                val actClass = Class.forName(act.name)
                loadInto(actClass, map)
            }
        }
    }

    fun loadInto(cls: Class<*>, map: MutableMap<String, RouteMeta>): Boolean {
        cls.getAnnotation(Router::class.java)?.let { router ->
            map[router.path] = RouteMeta().apply {
                path = router.path
                target = cls
                type = RouteType.ACTIVITY
            }
            return true
        }
        return false
    }
}
