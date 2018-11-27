package com.zaze.tribe.common.router

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-11-27 - 20:14
 */

enum class RouteType(val id: Int, val className: String) {
    ACTIVITY(0, "android.app.Activity"),
    FRAGMENT(1, "android.app.Fragment"),
    FRAGMENT_V4(2, "android.support.v4.app"),
    FRAGMENT_X(3, "androidx.fragment.app.Fragment")
}