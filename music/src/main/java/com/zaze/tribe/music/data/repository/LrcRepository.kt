package com.zaze.tribe.music.data.repository

import com.zaze.tribe.music.data.dto.Lyric
import com.zaze.tribe.music.data.db.LocalDatabase
import com.zaze.tribe.music.data.db.LyricDao

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:27
 */

class LrcRepository private constructor(
        private val lyricDao: LyricDao
) {
    companion object {
        private var INSTANCE: LrcRepository? = null

        @JvmStatic
        fun getInstance(): LrcRepository {
            if (INSTANCE == null) {
                synchronized(LrcRepository::javaClass) {
                    INSTANCE = LrcRepository(LocalDatabase.database.getLrcDao())
                }
            }
            return INSTANCE!!
        }
    }

    /**
     * 保存歌词信息
     */
    fun saveLrcInfo(lyric: Lyric?) {
        lyric?.let {
            lyricDao.insertOrUpdateLrcInfo(it)
        }
    }

    /**
     * 获取歌词信息
     */
    fun getLrcInfo(lrcId: Int) = lyricDao.getLrcInfo(lrcId)


}