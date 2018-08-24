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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMusicInfo(musicInfo: MusicInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMusicInfos(musicInfos: List<MusicInfo>)

    @Query("SELECT * FROM Music WHERE music_id = :musicId")
    fun getMusicInfo(musicId: Int): Flowable<MusicInfo>

    @Query("SELECT * FROM Music WHERE local_path = :localPath")
    fun getMusicByPath(localPath: String): Flowable<MusicInfo>

    @Query("SELECT * FROM Music")
    fun getMusicInfoList(): Flowable<List<MusicInfo>>
}