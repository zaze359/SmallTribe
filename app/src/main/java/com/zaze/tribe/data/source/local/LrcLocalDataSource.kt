package com.zaze.tribe.data.source.local

import com.zaze.tribe.data.dao.LyricDao
import com.zaze.tribe.data.dto.Lyric
import com.zaze.tribe.data.source.LrcDataSource

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:53
 */
class LrcLocalDataSource private constructor(
        private val lyricDao: LyricDao
) : LrcDataSource {

    companion object {
        private var INSTANCE: LrcLocalDataSource? = null
        @JvmStatic
        fun getInstance(lyricDao: LyricDao): LrcLocalDataSource {
            if (INSTANCE == null) {
                synchronized(LrcLocalDataSource::javaClass) {
                    INSTANCE = LrcLocalDataSource(lyricDao)
                }
            }
            return INSTANCE!!
        }
    }

    override fun saveLrcInfo(lyric: Lyric?) {
        lyric?.let {
            lyricDao.insertOrUpdateLrcInfo(lyric)
        }
    }

    override fun getLrcInfo(lrcId: Int) = lyricDao.getLrcInfo(lrcId)


}