package com.zaze.tribe.data.dto

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-07-05 - 22:11
 */
@Entity(tableName = "lyric")
data class Lyric(
        /**
         * id
         */
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "lrc_id")
        var id: Int = 0,

        /**
         * 下载地址
         */
        @ColumnInfo(name = "down_url")
        var downUrl: String,
        /**
         * 本地地址
         */
        @ColumnInfo(name = "local_path")
        var localPath: String,
        /**
         * 更新时间
         */
        var modified: Long = System.currentTimeMillis()

)
