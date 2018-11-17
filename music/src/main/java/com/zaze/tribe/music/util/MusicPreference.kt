
package com.zaze.tribe.music.util

import com.zaze.tribe.common.App
import com.zaze.tribe.music.R
import com.zaze.utils.ZSharedPrefUtil

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-10-17 - 23:31
 */
object MusicPreference {

    private val sharedPrefUtil = ZSharedPrefUtil.newInstance(App.INSTANCE, "music_pref")

    private const val LATELY_MUSIC_POSITION = "LATELY_MUSIC_POSITION"
    private const val LATELY_MUSIC_TRACK = "LATELY_MUSIC_TRACK"

    // ------------------------------------------------------
    /**
     * 保存最后听的歌曲
     */
    fun getLastMusicPosition(): Int {
        return sharedPrefUtil.get(LATELY_MUSIC_POSITION, -1)
    }

    /**
     * 保存最近听的歌曲
     */
    fun saveLastMusicPosition(position: Int) {
        sharedPrefUtil.apply(LATELY_MUSIC_POSITION, position)
    }

    fun getLastMusicTrack(): Int {
        return sharedPrefUtil.get(LATELY_MUSIC_TRACK, -1)
    }

    fun saveLastMusicTrack(track : Int) {
        sharedPrefUtil.apply(LATELY_MUSIC_TRACK, track)
    }


}