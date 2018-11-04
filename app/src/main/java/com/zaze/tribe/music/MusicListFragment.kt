package com.zaze.tribe.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.tribe.base.BaseFragment
import com.zaze.tribe.databinding.MusicListFragBinding
import com.zaze.tribe.util.obtainViewModel
import kotlinx.android.synthetic.main.music_list_frag.*
import java.util.*

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-06 - 00:30
 */
class MusicListFragment : BaseFragment() {

    private lateinit var viewDataBinding: MusicListFragBinding
    private lateinit var musicAdapter: MusicAdapter

    companion object {
        fun newInstance(): MusicListFragment {
            val args = Bundle()
            val fragment = MusicListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewDataBinding = MusicListFragBinding.inflate(inflater, container, false)
        obtainViewModel(MusicListViewModel::class.java)?.let {
            viewDataBinding.viewModel = it
            it.loadMusics()
        }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            musicRecyclerView.layoutManager = LinearLayoutManager(it)
            viewDataBinding.viewModel?.apply {
                musicAdapter = MusicAdapter(it, ArrayList(0), this)
                musicRecyclerView.adapter = musicAdapter
            }
        }
    }
}