package com.zaze.tribe.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.zaze.tribe.data.dto.MusicInfo
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
    fun insertMusicInfo(musicInfo: MusicInfo)

    /**
     * 批量保存歌曲信息
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMusicInfos(musicInfos: List<MusicInfo>)

    /**
     * 根据 music_id 获取歌曲信息
     */
    @Query("SELECT * FROM music WHERE id = :musicId")
    fun getMusicInfo(musicId: Int): Flowable<MusicInfo>

    /**
     * 根据 local_path 获取歌曲信息
     */
    @Query("SELECT * FROM music WHERE data = :localPath")
    fun getMusicByPath(localPath: String): Flowable<MusicInfo>

    /**
     * 获取歌曲列表
     */
    @Query("SELECT * FROM music")
    fun getMusicInfoList(): Flowable<List<MusicInfo>>
}