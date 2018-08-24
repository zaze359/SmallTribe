package com.zaze.tribe.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.zaze.tribe.data.dto.LrcInfo

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:00
 */
@Dao
interface LrcDao {

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateLrcInfo(lrcInfo: LrcInfo)

    @Query("SELECT * FROM Lrc WHERE lrc_id=:lrcId")
    fun getLrcInfo(lrcId: Int): LrcInfo?

    @Query("SELECT * FROM Lrc")
    fun getLrcInfoList(): List<LrcInfo>

}