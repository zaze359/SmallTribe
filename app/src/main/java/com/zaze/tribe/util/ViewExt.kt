package com.zaze.tribe.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zaze.tribe.base.BaseRecyclerAdapter
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.music.vm.MusicListViewModel
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