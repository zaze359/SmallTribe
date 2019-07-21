package com.zaze.tribe.reader.util

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zaze.tribe.reader.bookshelf.BookshelfViewModel

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-06-08 - 19:18
 */
@BindingAdapter("app:bookshelfLoader")
fun SwipeRefreshLayout.setSwipeRefreshLayoutOnRefreshListener(
        viewModel: BookshelfViewModel) {
    setOnRefreshListener { viewModel.loadBookshelf() }
}
