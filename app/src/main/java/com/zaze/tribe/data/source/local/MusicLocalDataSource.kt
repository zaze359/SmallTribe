package com.zaze.tribe.data.source.local

import com.zaze.tribe.data.dao.MusicDao
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.data.source.MusicDataSource
import io.reactivex.Flowable

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:53
 */
class MusicLocalDataSource private constructor(
        private val musicDao: MusicDao
) : MusicDataSource {
    override fun saveMusicInfos(musicInfos: List<MusicInfo>?) {
        musicInfos?.let {
            musicDao.insertMusicInfos(it)
        }
    }

    override fun getMusicInfo(localPath: String?): Flowable<MusicInfo> {
        return musicDao.getMusicByPath(localPath ?: "")
    }

    override fun saveMusicInfo(musicInfo: MusicInfo?) {
        musicInfo?.let {
            musicDao.insertMusicInfo(musicInfo)
        }
    }

    override fun getMusicInfo(musicId: Int) = musicDao.getMusicInfo(musicId)


    override fun getMusicInfoList(): Flowable<List<MusicInfo>> {
        return musicDao.getMusicInfoList()
    }


    companion object {
        private var INSTANCE: MusicLocalDataSource? = null
        @JvmStatic
        fun getInstance(musicDao: MusicDao): MusicLocalDataSource {
            if (INSTANCE == null) {
                synchronized(MusicLocalDataSource::javaClass) {
                    INSTANCE = MusicLocalDataSource(musicDao)
                }
            }
            return INSTANCE!!
        }

    }


}