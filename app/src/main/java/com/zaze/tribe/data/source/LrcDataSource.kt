package com.zaze.tribe.data.source

import com.zaze.tribe.data.dto.LrcInfo

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:27
 */
interface LrcDataSource {

    /**
     * 保存歌词信息
     */
    fun saveLrcInfo(lrcInfo: LrcInfo?)

    /**
     * 获取歌词信息
     */
    fun getLrcInfo(lrcId: Int): LrcInfo?
}