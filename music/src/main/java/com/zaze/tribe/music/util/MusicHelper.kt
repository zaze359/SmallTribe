package com.zaze.tribe.util

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-12 - 22:53
 */
object MusicHelper {
    private val artworkUri = Uri.parse("content://media/external/audio/albumart")

    fun getMediaStoreAlbumCoverUri(albumId: Int): Uri {
        return ContentUris.withAppendedId(artworkUri, albumId.toLong())
    }

    fun getMusicFileUri(musicId: Int): Uri {
        return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicId.toLong())
    }


    fun getGenres() {}

}