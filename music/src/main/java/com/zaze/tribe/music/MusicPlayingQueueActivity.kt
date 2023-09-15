package com.zaze.tribe.music

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.tribe.common.BaseActivity
import com.zaze.tribe.common.util.setupActionBar
import com.zaze.tribe.music.adapter.PlayingQueueAdapter
import com.zaze.tribe.music.databinding.MusicPlayingQueueActBinding
import com.zaze.tribe.music.vm.PlayingQueueViewModel

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

    private lateinit var binding: MusicPlayingQueueActBinding
    private val viewModel: PlayingQueueViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MusicPlayingQueueActBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar(binding.appbarLayout.toolbar) {
            setTitle(R.string.music_playlist)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            it.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        binding.musicPlayingQueueRv.let {
            it.layoutManager = LinearLayoutManager(this@MusicPlayingQueueActivity)
            it.adapter = PlayingQueueAdapter(this, MusicPlayerRemote.getPlayingQueue(), viewModel)
        }
    }
}