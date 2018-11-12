package com.zaze.tribe.data.source.repository

import com.zaze.tribe.data.dto.Music
import com.zaze.tribe.data.source.MusicDataSource
import com.zaze.tribe.data.source.local.LocalDatabase
import com.zaze.tribe.data.source.local.MusicLocalDataSource
import io.reactivex.Flowable

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:26
 */
class MusicRepository private constructor(
        private val localDataSource: MusicDataSource
) : MusicDataSource {

    override fun getMusicInfo(localPath: String?): Flowable<Music> {
        return localDataSource.getMusicInfo(localPath)
    }

    override fun saveMusicInfo(music: Music?) {
        localDataSource.saveMusicInfo(music)
    }

    override fun getMusicInfo(musicId: Int) = localDataSource.getMusicInfo(musicId)

    override fun saveMusicInfos(music: List<Music>?) {
        localDataSource.saveMusicInfos(music)
    }

    override fun getMusicInfoList(): Flowable<List<Music>> {
        return localDataSource.getMusicInfoList()
    }

    companion object {
        private var INSTANCE: MusicRepository? = null

        @JvmStatic
        fun getInstance(): MusicRepository {
            if (INSTANCE == null) {
                synchronized(MusicRepository::javaClass) {
                    INSTANCE = MusicRepository(
                            MusicLocalDataSource.getInstance(LocalDatabase.database.getMusicDao())
                    )
                }
            }
            return INSTANCE!!
        }
    }

}
