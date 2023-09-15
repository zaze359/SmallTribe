package com.zaze.tribe.music

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import com.zaze.tribe.common.base.AbsFragment
import com.zaze.tribe.music.databinding.MusicControlFragBinding
import com.zaze.tribe.music.handler.MusicProgressHandler
import com.zaze.tribe.common.util.Utils

/**
 * Description : 音频控制操作
 *
 * @author : ZAZE
 * @version : 2018-10-16 - 0:45
 */
class MusicControlFragment : AbsFragment(), MusicProgressHandler.Callback {

    private var _binding: MusicControlFragBinding? = null
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
        _binding = MusicControlFragBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.musicControlSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicPlayerRemote.seekTo(progress)
                    onProgress(MusicPlayerRemote.getProgress(), MusicPlayerRemote.getDuration())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        MusicPlayerRemote.curMusicData.observe(viewLifecycleOwner) {
            binding.musicArtistTv.text = it?.title
            binding.musicArtistTv.text = it?.artistName
        }
    }

    override fun onProgress(progress: Int, total: Int) {
        binding.musicControlSeekBar.max = total
        val animator = ObjectAnimator.ofInt(binding.musicControlSeekBar, "progress", progress)
        animator.duration = 2000L
        animator.interpolator = LinearInterpolator()
        animator.start()
        binding.musicControlCurTime.text = Utils.getDurationString(progress.toLong())
        binding.musicControlTotalTime.text = Utils.getDurationString(total.toLong())
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