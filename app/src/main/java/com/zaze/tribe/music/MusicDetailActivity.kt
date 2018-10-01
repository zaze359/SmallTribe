package com.zaze.tribe.music

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.zaze.common.base.BaseActivity
import com.zaze.tribe.R
import com.zaze.tribe.databinding.MusicDetailActBinding
import com.zaze.tribe.util.replaceFragmentInActivity

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-06 - 00:26
 */
class MusicDetailActivity : BaseActivity() {
    private lateinit var viewDataBinding: MusicDetailActBinding
    override fun isNeedHead(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.music_detail_act)
    }
}