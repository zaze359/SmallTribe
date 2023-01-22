package com.zaze.tribe.common.util

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaze.tribe.common.BaseAndroidViewModel
import com.zaze.tribe.common.BaseApplication

open class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (BaseAndroidViewModel::class.java.isAssignableFrom(modelClass)) {
            try {
                modelClass.getConstructor(Application::class.java)
                    .newInstance(BaseApplication.INSTANCE)
            } catch (e: Exception) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            }
        } else super.create(modelClass)
    }
}

fun initAbsViewModel(owner: ComponentActivity?, viewModel: ViewModel) {
//    if (owner is AbsActivity && viewModel is AbsViewModel) {
//        viewModel._showMessage.observe(owner, Observer {
//            owner.showToast(it)
//        })
//        viewModel._progress.observe(owner, Observer {
//            owner.progress(it)
//        })
//        viewModel._tipDialog.observe(owner, Observer {
//            it?.build(owner)?.show()
//        })
//        viewModel._finish.observe(owner, Observer {
//            owner.finish()
//        })
//    }
}