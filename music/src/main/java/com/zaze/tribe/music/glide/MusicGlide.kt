package com.zaze.tribe.music.glide

import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.zaze.tribe.music.data.dto.Music
import com.zaze.tribe.music.R
import com.zaze.tribe.music.glide.palette.BitmapPaletteWrapper
import com.zaze.tribe.music.util.MusicHelper

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-12 - 23:37
 */

object MusicGlide {

//    fun create(requestManager: RequestManager, music: Music): Builder {
//        return Builder(requestManager, music)
//    }
//
//    class Builder(val requestManager: RequestManager, val music: Music) {
//        fun build(): RequestBuilder<Drawable> {
//            return requestManager
//                .load(MusicHelper.getMediaStoreAlbumCoverUri(music.albumId))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .error(R.mipmap.ic_launcher)
//        }
//    }

    fun getMusicModel(music: Music): Any {
        return MusicHelper.getMediaStoreAlbumCoverUri(music.albumId)
    }
    fun <T> RequestBuilder<T>.musicCoverOptions(
        music: Music
    ): RequestBuilder<T> {
        return diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.mipmap.ic_launcher)
//            .placeholder(getDrawable(DEFAULT_SONG_IMAGE))
//            .signature(createSignature(song))
    }
//    fun RequestManager.asBitmapPalette(): RequestBuilder<BitmapPaletteWrapper> {
//        return this.`as`(BitmapPaletteWrapper::class.java)
//    }
}