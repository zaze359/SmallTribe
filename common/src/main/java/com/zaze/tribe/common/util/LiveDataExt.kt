package com.zaze.tribe.common.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-12-10 - 23:51
 */

/**
 * 扩展observable的get方式
 */
fun <T : Any> MutableLiveData<T>.set(value: T?) = postValue(value)

/**
 * 扩展observable的set方式
 */
fun <T : Any> MutableLiveData<T>.get() = value