package com.zaze.tribe.common.util

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import com.zaze.tribe.common.R

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-04 - 21:45
 */
fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int) {
    supportFragmentManager.transact {
        replace(frameId, fragment)
    }
}

fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}

fun AppCompatActivity.showFragmentToActivity(fragment: Fragment) {
    supportFragmentManager.transact {
        show(fragment)
    }
}

fun AppCompatActivity.hideFragmentToActivity(fragment: Fragment) {
    supportFragmentManager.transact {
        hide(fragment)
    }
}

fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

fun FragmentManager.transactAllowingStateLoss(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commitAllowingStateLoss()
}

// --------------------------------------------------
//
//fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
//    setSupportActionBar(findViewById(toolbarId))
//    supportActionBar?.run {
//        action()
//    }
//}

fun AppCompatActivity.setupActionBar(toolbar: Toolbar, action: ActionBar.() -> Unit = {}) {
    setSupportActionBar(toolbar)
    supportActionBar?.run {
        action()
    }
}

// --------------------------------------------------
/**
 * 设置沉浸式
 * [isFullScreen] isFullScreen
 * [color] color
 */
fun AppCompatActivity.setImmersion(
    color: Int = R.color.colorPrimary,
    isFullScreen: Boolean = false
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        if (isFullScreen) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        } else {
            window.statusBarColor = ContextCompat.getColor(this, color)
        }
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

// --------------------------------------------------
fun ComponentActivity.obtainViewModelFactory(): ViewModelFactory {
    return object : ViewModelFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return super.create(modelClass).also { vm ->
                initAbsViewModel(this@obtainViewModelFactory, vm)
            }
        }
    }
}

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.myViewModels(): Lazy<VM> = viewModels {
    obtainViewModelFactory()
}