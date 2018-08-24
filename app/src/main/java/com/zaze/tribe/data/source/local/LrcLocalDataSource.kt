package com.zaze.tribe.data.source.local

import com.zaze.tribe.data.dao.LrcDao
import com.zaze.tribe.data.dto.LrcInfo
import com.zaze.tribe.data.source.LrcDataSource

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:53
 */
class LrcLocalDataSource private constructor(
        private val lrcDao: LrcDao
) : LrcDataSource {

    override fun saveLrcInfo(lrcInfo: LrcInfo?) {
        lrcInfo?.let {
            lrcDao.insertOrUpdateLrcInfo(lrcInfo)
        }
    }

    override fun getLrcInfo(lrcId: Int) = lrcDao.getLrcInfo(lrcId)

    companion object {
        private var INSTANCE: LrcLocalDataSource? = null
        @JvmStatic
        fun getInstance(lrcDao: LrcDao): LrcLocalDataSource {
            if (INSTANCE == null) {
                synchronized(LrcLocalDataSource::javaClass) {
                    INSTANCE = LrcLocalDataSource(lrcDao)
                }
            }
            return INSTANCE!!
        }
    }
}