package com.zaze.tribe.common.base

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-15 - 15:20
 */
abstract class AbsLogActivity : AppCompatActivity() {

    companion object {
        var globalLog = false
        private const val TAG = "${ZTag.TAG}LifeCycle"
    }

    open fun showLifeCycle(): Boolean {
        return globalLog
    }

    private val activityName = this.javaClass.simpleName

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (showLifeCycle()) {
            ZLog.d(TAG, "$activityName onAttachedToWindow")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (showLifeCycle()) {
            ZLog.d(TAG, "$activityName onCreate")
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (showLifeCycle()) {
            ZLog.d(TAG, "$activityName onPostCreate")
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (showLifeCycle())
            ZLog.d(TAG, "$activityName onNewIntent")
    }

    override fun onStart() {
        super.onStart()
        if (showLifeCycle())
            ZLog.d(TAG, "$activityName onStart")
    }

    override fun onRestart() {
        super.onRestart()
        if (showLifeCycle()) {
            ZLog.d(TAG, "$activityName onRestart")
        }
    }

    override fun onResume() {
        super.onResume()
        if (showLifeCycle())
            ZLog.d(TAG, "$activityName onResume")
    }


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        if (showLifeCycle())
            ZLog.d(TAG, "$activityName onSaveInstanceState")
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (showLifeCycle())
            ZLog.d(TAG, "$activityName onRestoreInstanceState")
    }

    override fun onPause() {
        super.onPause()
        if (showLifeCycle())
            ZLog.d(TAG, "$activityName onPause")
    }

    override fun onStop() {
        super.onStop()
        if (showLifeCycle())
            ZLog.d(TAG, "$activityName onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (showLifeCycle())
            ZLog.d(TAG, "$activityName onDestroy")
    }
}

