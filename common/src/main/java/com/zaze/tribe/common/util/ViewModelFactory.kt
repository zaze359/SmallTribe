package com.zaze.tribe.common.util

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.zaze.tribe.common.base.AbsAndroidViewModel
import com.zaze.tribe.common.base.BaseApplication
import kotlin.reflect.KClass


class MyViewModelLazy<VM : ViewModel>(
    private val activity: () -> ComponentActivity?,
    private val viewModelClass: KClass<VM>,
    private val storeProducer: () -> ViewModelStore,
    private val factoryProducer: () -> ViewModelProvider.Factory
) : Lazy<VM> {
    private var cached: VM? = null
    private var observed = false

    override val value: VM
        get() {
            val viewModel = cached
            return if (viewModel == null) {
                val factory = factoryProducer()
                val store = storeProducer()
                ViewModelProvider(store, factory)[viewModelClass.java].also {
                    cached = it
                }
            } else {
                viewModel
            }.apply {
                observe(activity(), this)
            }
        }

    private fun observe(owner: ComponentActivity?, viewModel: ViewModel) {
        if (observed) {
            return
        }
        if (owner == null) {
            // Fragment not attached to an activity.
            observed = false
        } else {
            observed = true
            initAbsViewModel(owner, viewModel)
        }
    }

    override fun isInitialized(): Boolean = cached != null
}

fun obtainViewModelFactory(application: Application, delegateFactory: ViewModelProvider.Factory? = null): ViewModelFactory {
    return ViewModelFactory(application, delegateFactory)
}

open class ViewModelFactory(private val application: Application?, private val delegateFactory: ViewModelProvider.Factory? = null) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (application != null && AbsAndroidViewModel::class.java.isAssignableFrom(modelClass)) {
            try {
                modelClass.getConstructor(Application::class.java)
                    .newInstance(application)
            } catch (e: Exception) {
                throw RuntimeException("zz Cannot create an instance of $modelClass", e)
            }
        } else delegateFactory?.create(modelClass) ?: super.create(modelClass)
    }
}



/**
 * 在fragment中构建仅和fragment 关联的viewModel
 */
fun <T : ViewModel> Fragment.obtainFragViewModel(viewModelClass: Class<T>): T {
    return ViewModelProvider(this, ViewModelFactory(this.requireActivity().application))[viewModelClass]
}

/**
 * 在fragment中构建和activity 关联的viewModel
 */
@Deprecated("use obtainViewModelFactory ")
fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>): T {
    return requireActivity().let {
        ViewModelProvider(it, ViewModelFactory(this.requireActivity().application))[viewModelClass].also { vm ->
            initAbsViewModel(it, vm)
        }
    }
}

/**
 * 在activity中构建和activity 关联的viewModel
 */
@Deprecated("use myViewModels")
fun <T : ViewModel> AppCompatActivity.obtainViewModel(
    viewModelClass: Class<T>
) = ViewModelProvider(this, ViewModelFactory(this.application))[viewModelClass].also { vm ->
    initAbsViewModel(this, vm)
}