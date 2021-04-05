package com.zaze.tribe.music.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zaze.tribe.music.data.dto.Music
import io.reactivex.Flowable

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:00
 */
@Dao
interface MusicDao {

    /**
     * 保存歌曲信息
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMusicInfo(music: Music)

    /**
     * 批量保存歌曲信息
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMusicInfos(music: List<Music>)

    /**
     * 根据 music_id 获取歌曲信息
     */
    @Query("SELECT * FROM music WHERE id = :musicId")
    fun getMusicInfo(musicId: Int): Flowable<Music>

    /**
     * 根据 local_path 获取歌曲信息
     */
    @Query("SELECT * FROM music WHERE data = :localPath")
    fun getMusicByPath(localPath: String): Flowable<Music>

    /**
     * 获取歌曲列表
     */
    @Query("SELECT * FROM music")
    fun getPlayingQueue(): List<Music>
}