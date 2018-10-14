package com.zaze.tribe.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.zaze.tribe.data.dto.Lyric

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:00
 */
@Dao
interface LyricDao {

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateLrcInfo(lyric: Lyric)

    @Query("SELECT * FROM lyric WHERE lrc_id=:lrcId")
    fun getLrcInfo(lrcId: Int): Lyric?

    @Query("SELECT * FROM lyric")
    fun getLrcInfoList(): List<Lyric>

}