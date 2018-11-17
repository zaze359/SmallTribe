package com.zaze.tribe.common.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zaze.tribe.common.BaseRecyclerAdapter


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
