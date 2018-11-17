package com.zaze.tribe.music.data.source

import com.zaze.tribe.music.data.dto.Lyric

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:27
 */
interface LrcDataSource {

    /**
     * 保存歌词信息
     */
    fun saveLrcInfo(lyric: Lyric?)

    /**
     * 获取歌词信息
     */
    fun getLrcInfo(lrcId: Int): Lyric?
}