package com.zaze.tribe.data.loaders

import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.provider.MediaStore
import com.zaze.tribe.data.dto.Music

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-10-14 - 20:40
 */
object MusicLoader {

    /**
     * 获取所有title 不为空的 music
     */
    @JvmStatic
    fun getLocalMusics(context: Context): List<Music> {
        return buildMusics(query(context,
                "${MediaStore.Audio.AudioColumns.IS_MUSIC}=? AND ${MediaStore.Audio.AudioColumns.TITLE} != ?",
                arrayOf("1", ""), MediaStore.Audio.Media.DEFAULT_SORT_ORDER))
    }

    // ------------------------------------------------------

    private fun buildMusics(cursor: Cursor?): List<Music> {
        val musics = ArrayList<Music>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                musics.add(buildMusicFormCursor(cursor))
            } while (cursor.moveToNext())
        }
        return musics
    }

    private fun buildMusicFormCursor(cursor: Cursor): Music {
        val id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
        val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE))
        val track = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TRACK))
        val year = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.YEAR))
        val duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION))
        val data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA))
        val dateModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_MODIFIED))
        val albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID))
        val albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM))
        val artistId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST_ID))
        val artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST))
        return Music(id, title, track, year, duration, data, dateModified, albumId, albumName, artistId, artistName)

    }

    @JvmStatic
    fun query(context: Context, selection: String, selectionArgs: Array<String>, sortOrder: String): Cursor? {
        return context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(BaseColumns._ID,
                        MediaStore.Audio.AudioColumns.TITLE,
                        MediaStore.Audio.AudioColumns.TRACK,
                        MediaStore.Audio.AudioColumns.YEAR,
                        MediaStore.Audio.AudioColumns.DURATION,
                        MediaStore.Audio.AudioColumns.DATA,
                        MediaStore.Audio.AudioColumns.DATE_MODIFIED,
                        MediaStore.Audio.AudioColumns.ALBUM_ID,
                        MediaStore.Audio.AudioColumns.ALBUM,
                        MediaStore.Audio.AudioColumns.ARTIST_ID,
                        MediaStore.Audio.AudioColumns.ARTIST
                ), selection, selectionArgs, sortOrder)
    }


}