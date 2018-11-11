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

    private const val LATELY_PAGE = "lately_page"
    private const val LATELY_MUSIC = "lately_music"


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
    fun getLastMusic(): Int {
        return sharedPrefUtil.get(LATELY_MUSIC, 0)
    }

    /**
     * 保存最近听的歌曲
     */
    fun saveLastMusic(position: Int) {
        sharedPrefUtil.apply(LATELY_MUSIC, position)
    }
}