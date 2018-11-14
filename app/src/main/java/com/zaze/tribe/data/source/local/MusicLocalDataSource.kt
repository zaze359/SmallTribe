package com.zaze.tribe.data.source.local

import com.zaze.tribe.data.dao.MusicDao
import com.zaze.tribe.data.dto.Music
import com.zaze.tribe.data.source.MusicDataSource

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:53
 */
class MusicLocalDataSource private constructor(
        private val musicDao: MusicDao
) : MusicDataSource {

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

    override fun getMusicInfo(localPath: String?) = musicDao.getMusicByPath(localPath ?: "")

    override fun getMusicInfo(musicId: Int) = musicDao.getMusicInfo(musicId)

    override fun saveToPlayingQueue(music: Music?) {
        music?.let {
            musicDao.insertMusicInfo(music)
        }
    }

    override fun savePlayingQueue(music: List<Music>?) {
        music?.let {
            musicDao.insertMusicInfos(it)
        }
    }

    override fun getPlayingQueue() = musicDao.getPlayingQueue()
}