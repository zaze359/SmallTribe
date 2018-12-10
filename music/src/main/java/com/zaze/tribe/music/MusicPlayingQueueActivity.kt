package com.zaze.tribe.music

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.router.ZRouter
import com.zaze.tribe.common.BaseActivity
import com.zaze.tribe.common.util.obtainViewModel
import com.zaze.tribe.common.util.setupActionBar
import com.zaze.tribe.music.adapter.PlayingQueueAdapter
import com.zaze.tribe.music.databinding.MusicPlayingQueueActBinding
import com.zaze.tribe.music.vm.PlayingQueueViewModel
import kotlinx.android.synthetic.main.music_playing_queue_act.*

/**
 * Description : 播放列表
 *
 * @author : ZAZE
 * @version : 2018-11-11 - 23:17
 */
class MusicPlayingQueueActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            ContextCompat.startActivity(context, Intent(context, MusicPlayingQueueActivity::class.java), null)
        }
    }

    private lateinit var dataBinding: MusicPlayingQueueActBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.music_playing_queue_act)
        setupActionBar(musicPlayingQueueToolbar) {
            setTitle(R.string.music_playlist)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        musicPlayingQueueToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val viewModel = obtainViewModel(PlayingQueueViewModel::class.java)
        musicPlayingQueueRv.let {
            it.layoutManager = LinearLayoutManager(this@MusicPlayingQueueActivity)
            it.adapter = PlayingQueueAdapter(this, MusicPlayerRemote.getPlayingQueue(), viewModel)
        }
    }
}