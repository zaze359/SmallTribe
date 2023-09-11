package com.zaze.tribe.music.data.repository

import com.zaze.tribe.music.data.dto.Music
import com.zaze.tribe.music.data.db.LocalDatabase
import com.zaze.tribe.music.data.db.MusicDao
import io.reactivex.Flowable

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:26
 */
class MusicRepository private constructor(
        private val musicDao: MusicDao
) {
    companion object {
        private var INSTANCE: MusicRepository? = null

        @JvmStatic
        fun getInstance(): MusicRepository {
            if (INSTANCE == null) {
                synchronized(MusicRepository::javaClass) {
                    INSTANCE = MusicRepository(LocalDatabase.database.getMusicDao())
                }
            }
            return INSTANCE!!
        }
    }


    /**
     * 获取歌曲信息
     * [musicId] musicId
     * @return  Music
     */
    fun getMusicInfo(musicId: Int) = musicDao.getMusicInfo(musicId)

    /**
     * 获取歌曲信息
     * [musicId] musicId
     * @return  Music
     */
    fun getMusicInfo(localPath: String?): Flowable<Music> {
        return musicDao.getMusicByPath(localPath ?: "")
    }

    fun saveToPlayingQueue(music: Music?) {
        music?.let {
            musicDao.insertMusicInfo(music)
        }
    }

    /**
     * 保存歌曲列表
     */
    fun savePlayingQueue(musics: List<Music>?) {
        if (!musics.isNullOrEmpty()) {
            musicDao.insertMusicInfos(musics)
        }
    }


    fun getPlayingQueue() = musicDao.getPlayingQueue()

}
