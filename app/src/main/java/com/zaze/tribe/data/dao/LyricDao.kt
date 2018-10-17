package com.zaze.tribe.data.dao

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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