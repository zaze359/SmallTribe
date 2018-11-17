package com.zaze.tribe.music.data.source.repository

import com.zaze.tribe.music.data.dto.Lyric
import com.zaze.tribe.music.data.source.LrcDataSource
import com.zaze.tribe.music.data.source.local.LocalDatabase
import com.zaze.tribe.music.data.source.local.LrcLocalDataSource

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:27
 */

class LrcRepository private constructor(
        private val localDataSource: LrcLocalDataSource
) : LrcDataSource {
    override fun saveLrcInfo(lyric: Lyric?) {
        localDataSource.saveLrcInfo(lyric)
    }

    override fun getLrcInfo(lrcId: Int): Lyric? {
        return localDataSource.getLrcInfo(lrcId)
    }

    companion object {
        private var INSTANCE: LrcRepository? = null
        @JvmStatic
        fun getInstance(): LrcRepository {
            if (INSTANCE == null) {
                synchronized(LrcRepository::javaClass) {
                    INSTANCE = LrcRepository(
                            LrcLocalDataSource.getInstance(LocalDatabase.database.getLrcDao())
                    )
                }
            }
            return INSTANCE!!
        }
    }
}