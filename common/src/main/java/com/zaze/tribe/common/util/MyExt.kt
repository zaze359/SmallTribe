package com.zaze.tribe.common.util

import android.content.pm.ApplicationInfo
import android.view.View
import androidx.activity.ComponentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.zaze.tribe.common.base.AbsActivity
import com.zaze.tribe.common.base.AbsViewModel
import com.zaze.utils.ZOnClickHelper
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

// ---------------------------------

fun initAbsViewModel(owner: ComponentActivity?, viewModel: ViewModel) {
    if (owner is AbsActivity && viewModel is AbsViewModel) {
        viewModel._showMessage.observe(owner, Observer {
            ZLog.e(ZTag.TAG, "showToast: $it")
//            owner.showToast(it)
        })
        viewModel._progress.observe(owner, Observer {
            ZLog.e(ZTag.TAG, "progress: $it")
//            owner.progress(it)
        })
        viewModel._finish.observe(owner, Observer {
            owner.finish()
        })
    }
}
// ---------------------------------

fun <T : Any> MutableLiveData<T>.action() = postValue(null)

// ---------------------------------

//fun View.visible() {
//    this.visibility = View.VISIBLE
//}
//
//fun View.gone() {
//    this.visibility = View.GONE
//}
//
//fun View.invisible() {
//    this.visibility = View.INVISIBLE
//}

fun View.onClick(block: (View) -> Unit) {
    ZOnClickHelper.setOnClickListener(this) {
        block(it)
    }
}

// ---------------------------------
fun ApplicationInfo.isSystemApp(): Boolean {
    return this.flags and ApplicationInfo.FLAG_SYSTEM > 0
}