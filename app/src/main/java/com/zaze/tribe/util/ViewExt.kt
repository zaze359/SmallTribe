package com.zaze.tribe.util

import android.databinding.BindingAdapter
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.zaze.tribe.base.BaseRecyclerAdapter
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.music.MusicListViewModel
import com.zaze.utils.ThreadManager

/**
 * SwipeRefreshLayout
 * android:onRefresh --- MusicListViewModel
 * SwipeRefreshLayout.setOnRefreshListener()
 *
 */
@BindingAdapter("android:onRefresh")
fun SwipeRefreshLayout.setSwipeRefreshLayoutOnRefreshListener(
        viewModel: MusicListViewModel) {
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
 * app:bmp --- MusicInfo
 * ImageView.setImageBitmap()
 */
@BindingAdapter("app:bmp")
fun ImageView.setBitmap(music: MusicInfo?) {
    music?.let {
        ThreadManager.getInstance().runInSingleThread {
            IconCache.getSmallMediaIcon(it.data).apply {
                ThreadManager.getInstance().runInUIThread {
                    setImageBitmap(this)
                }
            }
        }
    }
}