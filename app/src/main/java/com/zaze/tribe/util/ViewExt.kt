package com.zaze.tribe.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.zaze.tribe.base.BaseRecyclerAdapter
import com.zaze.tribe.data.dto.Music
import com.zaze.tribe.music.vm.MusicViewModel
import com.zaze.tribe.util.glide.MusicGlide

/**
 * SwipeRefreshLayout
 * android:onRefresh --- MusicViewModel
 * SwipeRefreshLayout.setOnRefreshListener()
 *
 */
@BindingAdapter("android:onRefresh")
fun SwipeRefreshLayout.setSwipeRefreshLayoutOnRefreshListener(
        viewModel: MusicViewModel) {
    setOnRefreshListener { viewModel.loadMusics() }
}

/**
 * RecyclerView
 * app:items --- Collection<V>
 * BaseRecyclerAdapter.setDataList()
 */
@BindingAdapter("app:items")
fun <V> RecyclerView.setData(items: Collection<V>?) {
    adapter?.let {
        if (adapter is BaseRecyclerAdapter<*, *>) {
            (it as BaseRecyclerAdapter<V, *>).setDataList(items)
        }
    }
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