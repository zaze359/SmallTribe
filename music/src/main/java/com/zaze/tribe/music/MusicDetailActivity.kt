package com.zaze.tribe.music

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.zaze.tribe.common.BaseActivity
import com.zaze.tribe.common.util.get
import com.zaze.tribe.common.util.myViewModels
import com.zaze.tribe.music.databinding.MusicDetailActBinding
import com.zaze.tribe.music.vm.MusicViewModel
import kotlinx.android.synthetic.main.music_detail_act.*

/**
 * Description : 音乐详情页
 * @author : ZAZE
 * @version : 2018-07-06 - 00:26
 */
class MusicDetailActivity : BaseActivity(), Toolbar.OnMenuItemClickListener {

    private lateinit var viewDataBinding: MusicDetailActBinding

    private val viewModel: MusicViewModel by myViewModels()

    companion object {
        fun start(context: Context) {
            ContextCompat.startActivity(
                context,
                Intent(context, MusicDetailActivity::class.java),
                null
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.music_detail_act)
        viewModel.isFavorite.observe(this@MusicDetailActivity, Observer { isFavorite ->
            musicDetailToolbar.menu.findItem(R.id.action_toggle_favorite).apply {
                if (isFavorite) {
                    setIcon(R.drawable.music_favorite_black_24dp)
                    setTitle(R.string.music_favorites_remove)
                } else {
                    setIcon(R.drawable.music_favorite_border_black_24dp)
                    setTitle(R.string.music_favorites_add)
                }
            }
        })
        musicDetailToolbar.let {
            it.inflateMenu(R.menu.music_player_menu)
            it.setNavigationOnClickListener {
                onBackPressed()
            }
            it.setNavigationIcon(R.drawable.music_close_gray_24dp)
            it.setOnMenuItemClickListener(this)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        item?.apply {
            when (this.itemId) {
                R.id.action_toggle_favorite -> viewModel.toggleFavorite(MusicPlayerRemote.curMusicData.get())
            }
        }
        return true
    }

}