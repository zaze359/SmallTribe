package com.zaze.tribe.util.glide

import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.BitmapRequestBuilder
import com.bumptech.glide.DrawableRequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.zaze.tribe.R
import com.zaze.tribe.data.dto.Music
import com.zaze.tribe.util.MusicHelper

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-12 - 23:37
 */

object MusicGlide {

    fun create(requestManager: RequestManager, music: Music): Builder {
        return Builder(requestManager, music)
    }

    class Builder(val requestManager: RequestManager, val music: Music) {
        fun build(): DrawableRequestBuilder<Uri> {
            return requestManager
                    .loadFromMediaStore(MusicHelper.getMediaStoreAlbumCoverUri(music.albumId))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.mipmap.ic_launcher)
        }

        fun asBitmap(): BmpBuilder {
            return BmpBuilder(this)
        }
    }

    class BmpBuilder(private val builder: Builder) {
        fun build(): DrawableRequestBuilder<Uri> {
            return builder.requestManager
                    .loadFromMediaStore(MusicHelper.getMediaStoreAlbumCoverUri(builder.music.albumId))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.mipmap.ic_launcher)
        }
    }
}