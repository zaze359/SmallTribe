
package com.zaze.tribe.util

import com.zaze.tribe.App
import com.zaze.tribe.R
import com.zaze.utils.ZSharedPrefUtil

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-10-17 - 23:31
 */
object PreferenceUtil {

    private val sharedPrefUtil = ZSharedPrefUtil.newInstance(App.INSTANCE)

    private const val LATELY_PAGE = "LATELY_PAGE"
    private const val LATELY_MUSIC_POSITION = "LATELY_MUSIC_POSITION"
    private const val LATELY_MUSIC_TRACK = "LATELY_MUSIC_TRACK"


    // ------------------------------------------------------
    /**
     * 获取最后停留的页面
     */
    fun getLastPage(): Int {
        return sharedPrefUtil.get(LATELY_PAGE, R.id.action_home)
    }

    /**
     * 保存最后停留的页面
     */
    fun saveLastPage(pageId: Int) {
        sharedPrefUtil.apply(LATELY_PAGE, pageId)
    }

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