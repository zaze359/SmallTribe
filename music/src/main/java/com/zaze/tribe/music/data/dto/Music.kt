package com.zaze.tribe.music.data.dto

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 21:48
 */
@Entity(tableName = "music")
data class Music(
    /**
     * MediaStore 中的id
     */
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    /**
     * 歌名
     */
    @ColumnInfo(name = "title")
    val title: String?,
    /**
     * 轨道
     */
    @ColumnInfo(name = "track")
    val track: Int,
    /**
     *
     */
    @ColumnInfo(name = "year")
    val year: Int,
    /**
     * 持续时间
     */
    @ColumnInfo(name = "duration")
    val duration: Long,

    /**
     * 本地路径
     */
    @ColumnInfo(name = "data")
    val data: String?,
    /**
     *
     */
    @ColumnInfo(name = "date_modified")
    val dateModified: Long,
    /**
     *
     */
    @ColumnInfo(name = "album_id")
    val albumId: Long,
    /**
     * 专辑
     */
    @ColumnInfo(name = "album")
    val albumName: String?,
    /**
     *
     */
    @ColumnInfo(name = "artist_id")
    val artistId: Long,
    /**
     * 歌手
     */
    @ColumnInfo(name = "artist")
    val artistName: String?
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        return if (other is Music) {
            other.id == this.id
        } else {
            super.equals(other)
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeInt(track)
        parcel.writeInt(year)
        parcel.writeLong(duration)
        parcel.writeString(data)
        parcel.writeLong(dateModified)
        parcel.writeLong(albumId)
        parcel.writeString(albumName)
        parcel.writeLong(artistId)
        parcel.writeString(artistName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Music> {
        val EMPTY = Music(-1, "", -1, -1, -1L, "", -1L, -1L, "", -1L, "")
        override fun createFromParcel(parcel: Parcel): Music {
            return Music(parcel)
        }

        override fun newArray(size: Int): Array<Music?> {
            return arrayOfNulls(size)
        }
    }
}

//val Music.EMPTY
//    get() = Music(-1, "", -1, -1, -1L, "", -1L, -1L, "", -1L, "")