package com.zaze.tribe.music.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.music.MusicPlayerRemote

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-12 - 0:09
 */
class PlaylistViewModel(application: Application) : AndroidViewModel(application) {

    fun playAt(position: Int) {
        MusicPlayerRemote.playAt(position)
    }

    fun showMore(music: MusicInfo) {

    }
}