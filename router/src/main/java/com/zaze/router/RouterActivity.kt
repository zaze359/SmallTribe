package com.zaze.router

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Description : 特殊场景跳转使用
 * @author : ZAZE
 * @version : 2019-01-04 - 15:56
 */
class RouterActivity : AppCompatActivity() {
    companion object {
        val ROUTER = "router"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.getString(ROUTER)?.let {
            ZRouter.build(it).navigate(this)
        }
        finish()
    }
}