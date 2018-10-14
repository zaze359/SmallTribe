package com.zaze.tribe.util

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.zaze.tribe.data.source.repository.MusicRepository
import com.zaze.tribe.debug.DebugViewModel
import com.zaze.tribe.music.MusicViewModel


fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>) =
        activity?.run {
            ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)
        }

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-03 - 23:44
 */
class ViewModelFactory private constructor(
        private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) = INSTANCE
                ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: ViewModelFactory(application)
                            .also { INSTANCE = it }
                }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(DebugViewModel::class.java) ->
                        DebugViewModel(application)
                    isAssignableFrom(MusicViewModel::class.java) ->
                        MusicViewModel(application, MusicRepository.getInstance())
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}