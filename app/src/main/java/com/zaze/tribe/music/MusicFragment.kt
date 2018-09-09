package com.zaze.tribe.music

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaze.tribe.databinding.MusicFragBinding
import com.zaze.tribe.util.obtainViewModel
import kotlinx.android.synthetic.main.music_frag.*
import java.util.*

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-06 - 00:30
 */
class MusicFragment : Fragment() {

    private lateinit var viewDataBinding: MusicFragBinding
    private lateinit var musicAdapter: MusicAdapter
    private lateinit var viewModel: MusicViewModel

    companion object {
        fun newInstance(): MusicFragment {
            val args = Bundle()
            val fragment = MusicFragment()
            fragment.arguments = args
            return fragment
        }
    }

    fun setViewModel(viewModel : MusicViewModel) {
        this.viewModel = viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewDataBinding = MusicFragBinding.inflate(inflater, container, false)
        viewDataBinding.viewModel = viewModel
        viewModel.loadMusics()
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.let {
            music_recycler_view.layoutManager = LinearLayoutManager(activity)
            musicAdapter = MusicAdapter(activity, ArrayList(0), viewModel)
            music_recycler_view.adapter = musicAdapter
        }
    }
}