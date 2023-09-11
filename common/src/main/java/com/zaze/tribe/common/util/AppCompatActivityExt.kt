package com.zaze.tribe.common.util

import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
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

fun AppCompatActivity.FragmentToActivity(fragment: Fragment) {
    supportFragmentManager.transact {
        hide(fragment)
    }
}


fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

// --------------------------------------------------

fun AppCompatActivity.setupActionBar(toolbar: Toolbar, action: ActionBar.() -> Unit) {
    setSupportActionBar(toolbar)
    supportActionBar?.run {
        action()
    }
}

// --------------------------------------------------
/**
 * 设置沉浸式
 */
fun AppCompatActivity.setImmersion(color: Int = R.color.colorPrimary) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = ContextCompat.getColor(this, color)
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}


