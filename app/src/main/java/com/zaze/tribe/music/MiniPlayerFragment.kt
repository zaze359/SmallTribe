package com.zaze.tribe.music

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaze.tribe.databinding.MiniPlayerFragBinding

class MiniPlayerFragment : Fragment() {

    private lateinit var viewDataBinding: MiniPlayerFragBinding
    private lateinit var viewModel: MusicViewModel

    fun setViewModel(viewModel: MusicViewModel) {
        this.viewModel = viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = MiniPlayerFragBinding.inflate(inflater, container, false)
        viewDataBinding.musicViewModel = viewModel
        return viewDataBinding.root
    }

}