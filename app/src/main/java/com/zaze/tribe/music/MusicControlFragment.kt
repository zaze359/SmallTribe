package com.zaze.tribe.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaze.tribe.base.BaseFragment
import com.zaze.tribe.databinding.MusicControlFragBinding

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-10-16 - 0:45
 */
class MusicControlFragment : BaseFragment() {
    lateinit var dataBinding: MusicControlFragBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = MusicControlFragBinding.inflate(inflater, container, false)
        return dataBinding.root
    }
}