package com.zaze.tribe.util

import android.databinding.BindingAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.zaze.common.adapter.BaseRecyclerAdapter
import com.zaze.tribe.base.CustomFragmentPagerAdapter
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.music.MusicViewModel
import com.zaze.utils.ThreadManager

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

@BindingAdapter("app:items")
fun <V> ViewPager.setData(items: Collection<V>?) {
    adapter?.let {
        if (adapter is CustomFragmentPagerAdapter<*>) {
            (it as CustomFragmentPagerAdapter<V>).setDataList(items)
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