package com.zaze.tribe.music

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.zaze.tribe.R
import com.zaze.tribe.base.BaseActivity
import com.zaze.tribe.databinding.MusicDetailActBinding

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-06 - 00:26
 */
class MusicDetailActivity : BaseActivity() {
    private lateinit var viewDataBinding: MusicDetailActBinding

    companion object {
        fun start(context: Context) {
            ContextCompat.startActivity(context, Intent(context, MusicDetailActivity::class.java), null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.music_detail_act)
    }
}