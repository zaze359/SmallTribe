package com.zaze.tribe.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.tribe.common.base.AbsFragment
import com.zaze.tribe.music.adapter.LocalMusicAdapter
import com.zaze.tribe.music.databinding.MusicLocalFragBinding
import com.zaze.tribe.music.vm.MusicViewModel

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-06 - 00:30
 */
class LocalMusicFragment : AbsFragment() {

    private lateinit var binding: MusicLocalFragBinding
    private lateinit var localMusicAdapter: LocalMusicAdapter
    private val viewModel: MusicViewModel by activityViewModels()

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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.music_local_frag,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.musicRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            localMusicAdapter = LocalMusicAdapter(context, viewModel)
            adapter = localMusicAdapter
        }
        viewModel.loadMusics()
    }
}