package com.zaze.tribe.reader.bookshelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.zaze.tribe.common.BaseFragment
import com.zaze.tribe.common.util.obtainViewModel
import com.zaze.tribe.reader.bean.Book
import com.zaze.tribe.reader.databinding.BookshelfFragBinding
import kotlinx.android.synthetic.main.bookshelf_frag.*

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-06-08 - 0:02
 */
class BookshelfFragment : BaseFragment() {

    private lateinit var viewDataBinding: BookshelfFragBinding
    private var adapter: BookshelfAdapter? = null
    private lateinit var viewModel: BookshelfViewModel

    companion object {
        fun newInstance(): BookshelfFragment {
            return BookshelfFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = BookshelfFragBinding.inflate(inflater, container, false)
        viewDataBinding.setLifecycleOwner(this)
        viewDataBinding.viewModel = obtainViewModel(BookshelfViewModel::class.java).apply {
            this.bookData.observe(this@BookshelfFragment, Observer {
                showBookshelf(it)
            })
            viewModel = this
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookshelfRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
        }
        viewModel.loadBookshelf()
    }

    private fun showBookshelf(data: Collection<Book>) {
        context?.let { context ->
            adapter?.setDataList(data) ?: let {
                adapter = BookshelfAdapter(context, data)
                bookshelfRecyclerView.adapter = adapter
            }
        }

    }
}