package com.zaze.tribe.music

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaze.tribe.databinding.MusicListFragBinding
import kotlinx.android.synthetic.main.music_list_frag.*
import java.util.*

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-06 - 00:30
 */
class MusicListFragment : Fragment() {

    private lateinit var viewDataBinding: MusicListFragBinding
    private lateinit var musicAdapter: MusicAdapter
    private lateinit var viewModel: MusicViewModel

    companion object {
        fun newInstance(): MusicListFragment {
            val args = Bundle()
            val fragment = MusicListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    fun setViewModel(viewModel: MusicViewModel) {
        this.viewModel = viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewDataBinding = MusicListFragBinding.inflate(inflater, container, false)
        viewDataBinding.viewModel = viewModel
        viewModel.loadMusics()
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            music_recycler_view.layoutManager = LinearLayoutManager(it)
            musicAdapter = MusicAdapter(it, ArrayList(0), viewModel)
            music_recycler_view.adapter = musicAdapter
        }
    }
}