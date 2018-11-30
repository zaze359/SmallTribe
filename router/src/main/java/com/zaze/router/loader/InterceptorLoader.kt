package com.zaze.router.loader

import com.zaze.router.anno.Interceptor
import com.zaze.router.interceptor.IInterceptor

/**
 * Description : 加载所有Router
 *
 * @author : ZAZE
 * @version : 2018-11-27 - 10:02
 */
class InterceptorLoader {

    fun loadInto(cls: Class<*>, interceptors: ArrayList<IInterceptor>): Boolean {
        try {
            if (IInterceptor::class.java.isAssignableFrom(cls)) {
                cls.getAnnotation(Interceptor::class.java)?.let {
                    interceptors.add(cls.getConstructor().newInstance() as IInterceptor)
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false

    }
}
