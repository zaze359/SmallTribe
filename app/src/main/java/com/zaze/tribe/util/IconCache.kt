package com.zaze.tribe.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.support.v4.content.res.ResourcesCompat
import android.util.LruCache
import com.zaze.tribe.App
import com.zaze.tribe.R
import com.zaze.utils.BmpUtil
import com.zaze.utils.FileUtil
import java.lang.ref.SoftReference

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-17 - 09:54
 */
object IconCache {

    private const val BMP_SIZE = 48

    private val mediaMetadataRetriever by lazy(LazyThreadSafetyMode.SYNCHRONIZED, {
        MediaMetadataRetriever()
    })

    private val BITMAP_CACHE = LruCache<String, Bitmap>(100)

    var defaultMediaIcon: SoftReference<Bitmap>? = null

    @JvmStatic
    fun saveMediaIcon(path: String, bmp: Bitmap?) {
        bmp?.let {
            BITMAP_CACHE.put(path, bmp)
        }
    }

    @JvmStatic
    fun getMediaIcon(path: String): Bitmap {
        return if (FileUtil.exists(path)) {
            BITMAP_CACHE.get(path) ?: mediaMetadataRetriever.let {
                it.setDataSource(path)
                it.embeddedPicture?.let {
                    val options = BitmapFactory.Options().apply {
                        inJustDecodeBounds = true
                    }
                    BitmapFactory.decodeByteArray(it, 0, it.size, options)
                    BitmapFactory.decodeByteArray(it, 0, it.size, options.apply {
                        inJustDecodeBounds = false
                        if (outWidth == 0 || outHeight == 0) {
                            outWidth = BMP_SIZE
                            outHeight = BMP_SIZE
                        } else {
                            inSampleSize = if (outWidth >= outHeight) {
                                outWidth / BMP_SIZE
                            } else {
                                outHeight / BMP_SIZE
                            }
                        }
                    }).apply {
                        saveMediaIcon(path, this)
                    }
                } ?: getDefaultMediaIcon()
            }
        } else {
            getDefaultMediaIcon()
        }
    }

    private fun getDefaultMediaIcon(): Bitmap {
        return defaultMediaIcon?.get()
                ?: BmpUtil.drawable2Bitmap(getFullResIcon(R.mipmap.ic_launcher), BMP_SIZE)
                        ?.apply {
                            defaultMediaIcon = SoftReference(this)
                        }
                ?: Bitmap.createBitmap(BMP_SIZE, BMP_SIZE, Bitmap.Config.ALPHA_8)
    }

    private fun getFullResIcon(iconId: Int): Drawable? {
        return ResourcesCompat.getDrawable(App.INSTANCE.resources, iconId, null)
    }


}