package com.zaze.tribe.reader.bookshelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.zaze.tribe.common.base.AbsFragment
import com.zaze.tribe.reader.R
import com.zaze.tribe.reader.bean.Book
import com.zaze.tribe.reader.databinding.BookshelfFragBinding

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-06-08 - 0:02
 */
class BookshelfFragment : AbsFragment() {

    private var _binding: BookshelfFragBinding? = null
    private val binding get() = _binding!!
    private var adapter: BookshelfAdapter? = null
    private val viewModel: BookshelfViewModel by activityViewModels()

    companion object {
        fun newInstance(): BookshelfFragment {
            return BookshelfFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.bookshelf_frag, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.bookData.observe(viewLifecycleOwner, Observer {
            showBookshelf(it ?: emptyList())
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bookshelfRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
        }
        viewModel.loadBookshelf()
    }

    private fun showBookshelf(data: Collection<Book>) {
        context?.let { context ->
            adapter?.setDataList(data) ?: let {
                adapter = BookshelfAdapter(context, data)
                binding.bookshelfRecyclerView.adapter = adapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}