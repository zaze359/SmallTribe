package com.zaze.tribe.music

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.zaze.tribe.common.base.AbsFragment
import com.zaze.tribe.music.R.id.*
import com.zaze.tribe.music.databinding.MusicMiniPlayerFragBinding
import com.zaze.tribe.music.glide.MusicGlide
import com.zaze.tribe.music.glide.MusicGlide.musicCoverOptions
import com.zaze.tribe.music.handler.MusicProgressHandler
import com.zaze.tribe.music.util.MusicHelper

/**
 * Description : 迷你播放控制器
 *
 * @author : ZAZE
 * @version : 2018-09-30 - 0:37
 */
class MiniPlayerFragment : AbsFragment(), MusicProgressHandler.Callback {

    private var _binding: MusicMiniPlayerFragBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressHandler: MusicProgressHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressHandler = MusicProgressHandler(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MusicMiniPlayerFragBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.musicMiniPlayerStatus.setOnClickListener {
            if (MusicPlayerRemote.isPlaying.value == true) {
                MusicPlayerRemote.pause()
            } else {
                MusicPlayerRemote.resumePlaying()
            }
        }
        binding.musicMiniPlayerList.setOnClickListener {
            activity?.apply {
                MusicPlayingQueueActivity.start(this)
            }
        }
        binding.root.setOnClickListener {
            activity?.let { activity ->
                MusicDetailActivity.start(activity)
            }
        }
        MusicPlayerRemote.curMusicData.observe(viewLifecycleOwner) {
            binding.root.isVisible = it != null
            it?.let {
                Glide.with(requireContext())
                    .asBitmap()
                    .load(MusicGlide.getMusicModel(it))
                    .musicCoverOptions(it)
                    .into(binding.musicAlbumIv)
            }
            binding.musicMiniPlayerArtist.text = it?.artistName
            binding.musicMiniPlayerTitle.text = it?.title
        }
        MusicPlayerRemote.isPlaying.observe(viewLifecycleOwner) {
            if (it) {
                binding.musicMiniPlayerStatus.setImageResource(R.drawable.music_pause_circle_outline_black_24dp)
            } else {
                binding.musicMiniPlayerStatus.setImageResource(R.drawable.music_play_circle_outline_black_24dp)
            }
        }
    }

    override fun onProgress(progress: Int, total: Int) {
        binding.musicMiniPlayerProgressBar.max = total
        val animation = ObjectAnimator.ofInt(musicMiniPlayerProgressBar, "progress", progress)
        animation.duration = 1000L
        animation.interpolator = LinearInterpolator()
        animation.start()
    }

    override fun onResume() {
        super.onResume()
        progressHandler.start()
    }

    override fun onPause() {
        super.onPause()
        progressHandler.stop()
    }
}