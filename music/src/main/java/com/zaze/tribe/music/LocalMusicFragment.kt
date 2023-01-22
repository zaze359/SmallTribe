package com.zaze.tribe.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.tribe.common.BaseFragment
import com.zaze.tribe.common.util.myActivityViewModels
import com.zaze.tribe.music.adapter.LocalMusicAdapter
import com.zaze.tribe.music.databinding.MusicLocalFragBinding
import com.zaze.tribe.music.vm.MusicViewModel
import kotlinx.android.synthetic.main.music_local_frag.*

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-06 - 00:30
 */
class LocalMusicFragment : BaseFragment() {

    private lateinit var viewDataBinding: MusicLocalFragBinding
    private lateinit var localMusicAdapter: LocalMusicAdapter
    private val viewModel: MusicViewModel by myActivityViewModels()

    companion object {
        fun newInstance(): LocalMusicFragment {
            return LocalMusicFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = MusicLocalFragBinding.inflate(inflater, container, false)
        viewDataBinding.setLifecycleOwner(this)
        viewDataBinding.viewModel = viewModel
        viewModel.loadMusics()
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        musicRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            localMusicAdapter = LocalMusicAdapter(context, viewModel)
            adapter = localMusicAdapter
        }

    }
}