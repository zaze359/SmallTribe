package com.zaze.tribe.common.router

import android.content.Context
import android.os.Bundle

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-11-26 - 23:47
 */
class Postcard(path: String) : RouteMeta() {

    var extras: Bundle? = null

    var options: Bundle? = null

    var flags: Int = -1

    var action: String? = null

    init {
        this.path = path
    }


    fun navigate(context: Context? = null, requestCode: Int = 0, callback: NavigationCallback? = null) {
        ZRouter.navigate(context, this, requestCode, callback)
    }
}