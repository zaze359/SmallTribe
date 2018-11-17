package com.zaze.tribe.music

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.tribe.music.R
import com.zaze.tribe.common.BaseActivity
import com.zaze.tribe.common.util.obtainViewModel
import com.zaze.tribe.common.util.setupActionBar
import com.zaze.tribe.music.adapter.PlaylistAdapter
import com.zaze.tribe.music.databinding.PlaylistActBinding
import com.zaze.tribe.music.vm.PlaylistViewModel
import kotlinx.android.synthetic.main.playlist_act.*

/**
 * Description : 播放列表
 *
 * @author : ZAZE
 * @version : 2018-11-11 - 23:17
 */
class PlayingQueueActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            ContextCompat.startActivity(context, Intent(context, PlayingQueueActivity::class.java), null)
        }
    }

    private lateinit var dataBinding: PlaylistActBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.playlist_act)
        setupActionBar(playlistToolbar) {
            setTitle(R.string.music_playlist)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        playlistToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val viewModel = obtainViewModel(PlaylistViewModel::class.java)
        playlistRecyclerView.let {
            it.layoutManager = LinearLayoutManager(this@PlayingQueueActivity)
            it.adapter = PlaylistAdapter(this, MusicPlayerRemote.getPlayingQueue(), viewModel)
        }
    }
}