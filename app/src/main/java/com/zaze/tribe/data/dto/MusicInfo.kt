package com.zaze.tribe.data.dto

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.graphics.Bitmap

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 21:48
 */
@Entity(tableName = "Music")
data class MusicInfo (
        /**
         * id
         */
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "music_id")
        var id: Int = 0,
        /**
         * 歌名
         */
        var name: String = "",
        /**
         * 歌手
         */
        var artist: String = "",

        @ColumnInfo(name = "artist_id")
        var artistId: Long = 0L,

        /**
         * 专辑
         */
        var album: String = "",

        @ColumnInfo(name = "album_id")
        var albumId: Long = 0L,

        @Ignore
        var albumIcon: Bitmap? = null,
        /**
         * 下载地址
         */
        @ColumnInfo(name = "down_url")
        var downUrl: String = "",
        /**
         * 本地路径
         */
        @ColumnInfo(name = "local_path")
        var localPath: String = "",

        /**
         *
         */
        var year: Int = 0,
        /**
         * 持续时间
         */
        var duration: Long = 0L,

        /**
         * 变更时间
         */
        var modified: Long = System.currentTimeMillis()
)