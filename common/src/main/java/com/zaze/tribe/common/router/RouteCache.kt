package com.zaze.tribe.common.router

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.zaze.utils.AppUtil.getPackageInfo

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-28 - 0:48
 */
object RouteCache {
    val LAST_VERSION_CODE = "LAST_VERSION_CODE"

    val ALL_CLASS_NAMES = "ALL_CLASS_NAMES"


    var NEW_VERSION_CODE = -1

    fun isNewVersion(context: Context): Boolean {
        getPackageInfo(context)?.let {
            val versionCode = it.versionCode
            val sp = getSharedPreferences(context)
            return if (versionCode != sp.getInt(LAST_VERSION_CODE, -1)) {
                NEW_VERSION_CODE = versionCode
                true
            } else {
                false
            }
        }
        return true
    }

    fun updateVersion(context: Context) {
        if (NEW_VERSION_CODE > 0) {
            getSharedPreferences(context).edit().putInt(LAST_VERSION_CODE, NEW_VERSION_CODE).apply()
        }
    }

    fun updateAllClassNames(context: Context, classNames: Set<String>) {
        getSharedPreferences(context).edit().putStringSet(ALL_CLASS_NAMES, classNames).apply()
    }

    fun getAllClassNames(context: Context): Set<String>? {
        return getSharedPreferences(context).getStringSet(ALL_CLASS_NAMES, HashSet<String>())
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("RouteCache", Context.MODE_PRIVATE)
    }
    // ------------------------------------------------------

    private fun getPackageInfo(context: Context): PackageInfo? {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_CONFIGURATIONS)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return packageInfo
    }


    // ------------------------------------------------------


}
