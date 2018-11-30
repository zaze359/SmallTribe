package com.zaze.router.proxy

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-11-28 - 14:39
 */
class ModelProxy : InvocationHandler {

    companion object {
        fun <T> getModel(className: Class<T>): T {
            return Proxy.newProxyInstance(className.classLoader, arrayOf(className), ModelProxy()) as T
        }
    }

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        method?.let {
            method.isAccessible = true
            return Class.forName(method.returnType.name).newInstance()
        }
        return null
    }

}