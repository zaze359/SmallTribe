package com.zaze.tribe.music

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import androidx.databinding.Observable
import androidx.databinding.ObservableInt
import com.zaze.tribe.base.BaseFragment
import com.zaze.tribe.databinding.MusicControlFragBinding
import kotlinx.android.synthetic.main.music_control_frag.*

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-10-16 - 0:45
 */
class MusicControlFragment : BaseFragment() {
    lateinit var dataBinding: MusicControlFragBinding

    private val onProgressChangedCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            if (sender is ObservableInt) {
                val animator = ObjectAnimator.ofInt(musicControlSeekBar, "progress", sender.get())
                animator.duration = 1000L
                animator.interpolator = LinearInterpolator()
                animator.start()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = MusicControlFragBinding.inflate(inflater, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        musicControlSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicPlayerRemote.seekTo(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    override fun onResume() {
        super.onResume()
        MusicPlayerRemote.progress.addOnPropertyChangedCallback(onProgressChangedCallback)
    }

    override fun onPause() {
        super.onPause()
        MusicPlayerRemote.progress.removeOnPropertyChangedCallback(onProgressChangedCallback)
    }
}