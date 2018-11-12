package com.zaze.tribe.music

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.bumptech.glide.Glide
import com.zaze.tribe.base.BaseFragment
import com.zaze.tribe.data.dto.Music
import com.zaze.tribe.databinding.MiniPlayerFragBinding
import com.zaze.tribe.music.handler.MusicProgressHandler
import com.zaze.tribe.util.MusicHelper
import com.zaze.tribe.util.glide.MusicGlide
import kotlinx.android.synthetic.main.mini_player_frag.*

/**
 * Description : 迷你播放控制器
 *
 * @author : ZAZE
 * @version : 2018-09-30 - 0:37
 */
class MiniPlayerFragment : BaseFragment(), MusicProgressHandler.Callback {

    private lateinit var viewDataBinding: MiniPlayerFragBinding
    private lateinit var progressHandler: MusicProgressHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressHandler = MusicProgressHandler(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = MiniPlayerFragBinding.inflate(inflater, container, false)
        viewDataBinding.root.setOnClickListener {
            activity?.let { activity ->
                MusicDetailActivity.start(activity)
            }
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        miniPlayerStatus.setOnClickListener {
            if (MusicPlayerRemote.isPlaying.get()) {
                MusicPlayerRemote.pause()
            } else {
                MusicPlayerRemote.resumePlaying()
            }
        }
        miniPlayerList.setOnClickListener {
            activity?.apply {
                PlaylistActivity.start(this)
            }
        }
    }

    override fun onProgress(progress: Int, total: Int) {
        miniPlayerProgressBar.max = total
        val animation = ObjectAnimator.ofInt(miniPlayerProgressBar, "progress", progress)
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