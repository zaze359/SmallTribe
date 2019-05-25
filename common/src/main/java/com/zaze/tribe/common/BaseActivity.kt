package com.zaze.tribe.common

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.zaze.tribe.common.util.setImmersion
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-10-16 - 1:02
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setImmersion()
        ZLog.i(ZTag.TAG_DEBUG, "${this.javaClass.name} >> onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        ZLog.i(ZTag.TAG_DEBUG, "${this.javaClass.name} >> onDestroy")
    }
}