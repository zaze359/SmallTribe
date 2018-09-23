package com.zaze.tribe.util

import android.databinding.BindingAdapter
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.zaze.common.adapter.BaseRecyclerAdapter
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.music.MusicViewModel
import com.zaze.utils.ThreadManager

@BindingAdapter("android:onRefresh")
fun SwipeRefreshLayout.setSwipeRefreshLayoutOnRefreshListener(
        viewModel: MusicViewModel) {
    setOnRefreshListener { viewModel.loadMusics() }
}

@BindingAdapter("app:items")
fun <V> RecyclerView.setData(items: Collection<V>) {
    with(adapter as BaseRecyclerAdapter<V, *>) {
        setDataList(items)
    }
}

@BindingAdapter("app:bmp")
fun ImageView.setBitmap(music: MusicInfo?) {
    music?.let {
        it.albumIcon?.apply {
            setImageBitmap(it.albumIcon)
        } ?: ThreadManager.getInstance().runInSingleThread {
            IconCache.getSmallMediaIcon(it.localPath).apply {
                ThreadManager.getInstance().runInUIThread {
                    it.albumIcon = this
                    setImageBitmap(this)
                }
            }
        }

    }
}