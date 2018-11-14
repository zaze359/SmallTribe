package com.zaze.tribe.data.source.repository

import com.zaze.tribe.data.dto.Music
import com.zaze.tribe.data.source.MusicDataSource
import com.zaze.tribe.data.source.local.LocalDatabase
import com.zaze.tribe.data.source.local.MusicLocalDataSource

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:26
 */
class MusicRepository private constructor(
        private val localDataSource: MusicDataSource
) : MusicDataSource {

    override fun getMusicInfo(localPath: String?) = localDataSource.getMusicInfo(localPath)

    override fun saveToPlayingQueue(music: Music?) {
        localDataSource.saveToPlayingQueue(music)
    }

    override fun getMusicInfo(musicId: Int) = localDataSource.getMusicInfo(musicId)

    override fun savePlayingQueue(music: List<Music>?) {
        localDataSource.savePlayingQueue(music)
    }

    override fun getPlayingQueue() = localDataSource.getPlayingQueue()

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
