package com.zaze.tribe.music.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.zaze.tribe.music.data.dto.Music
import com.zaze.tribe.music.glide.MusicGlide
import com.zaze.tribe.music.vm.MusicViewModel


/**
 * SwipeRefreshLayout
 * android:loadMusics --- MusicViewModel
 * SwipeRefreshLayout.setOnRefreshListener()
 *
 */
@BindingAdapter("app:musicLoader")
fun SwipeRefreshLayout.setSwipeRefreshLayoutOnRefreshListener(
        viewModel: MusicViewModel) {
    setOnRefreshListener { viewModel.loadMusics() }
}

/**
 * ImageView
 * app:bmp --- Music
 * ImageView.setImageBitmap()
 */
@BindingAdapter("app:bmp")
fun ImageView.setBitmap(music: Music?) {
    music?.let {
        MusicGlide.create(Glide.with(context), it)
                .build()
                .into(this)
    }
}