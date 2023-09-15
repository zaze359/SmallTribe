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
@BindingAdapter("bookshelfLoader")
fun SwipeRefreshLayout.bookshelfLoader(
        viewModel: BookshelfViewModel) {
    setOnRefreshListener { viewModel.loadBookshelf() }
}
