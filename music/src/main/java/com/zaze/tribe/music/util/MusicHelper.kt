package com.zaze.tribe.music.util

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-12 - 22:53
 */
object MusicHelper {
    private val artworkUri = "content://media/external/audio/albumart".toUri()

    fun getMediaStoreAlbumCoverUri(albumId: Long): Uri {
        return ContentUris.withAppendedId(artworkUri, albumId)
    }

    fun getMusicFileUri(musicId: Int): Uri {
        return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicId.toLong())
    }


    fun getGenres() {}

}