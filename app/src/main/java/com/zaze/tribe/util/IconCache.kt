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
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.lang.ref.SoftReference

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-17 - 09:54
 */
object IconCache {

    private const val BMP_SIZE = 48

    private val mediaMetadataRetriever by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        MediaMetadataRetriever()
    }

    private val BITMAP_CACHE = LruCache<String, Bitmap>(40)

    private var defaultMediaIcon: SoftReference<Bitmap>? = null

    @JvmStatic
    fun saveSmallMediaIcon(path: String, bmp: Bitmap?) {
        bmp?.let {
            BITMAP_CACHE.put(path, bmp)
        }
    }

    @JvmStatic
    fun getSmallMediaIcon(path: String): Bitmap {
        return if (FileUtil.exists(path)) {
            BITMAP_CACHE.get(path) ?: buildMediaIcon(path, BMP_SIZE, BMP_SIZE)?.apply {
                saveSmallMediaIcon(path, this)
            } ?: getDefaultMediaIcon(BMP_SIZE)
        } else {
            getDefaultMediaIcon(BMP_SIZE)
        }
    }

    @JvmStatic
    fun buildMediaIcon(path: String, width: Int, height: Int): Bitmap? {
        return mediaMetadataRetriever.run {
            setDataSource(path)
            embeddedPicture?.run {
                val option = BitmapFactory.Options()
                BitmapFactory.decodeByteArray(this, 0, size, option.apply {
                    inJustDecodeBounds = true
                })
                BitmapFactory.decodeByteArray(this, 0, size, option.apply {
                    inJustDecodeBounds = false
                    inPreferredConfig = Bitmap.Config.RGB_565
                    if (outWidth == 0 || outHeight == 0) {
                        outWidth = width
                        outHeight = height
                    } else {
                        inSampleSize = if (outWidth >= outHeight) {
                            outWidth / width
                        } else {
                            outHeight / height
                        }
                    }
                })
            }
        }
    }

    @JvmStatic
    private fun getDefaultMediaIcon(size: Int): Bitmap {
        return defaultMediaIcon?.get()
                ?: BmpUtil.drawable2Bitmap(getFullResIcon(R.mipmap.ic_launcher), size)
                        ?.apply {
                            defaultMediaIcon = SoftReference(this)
                        }
                ?: Bitmap.createBitmap(size, size, Bitmap.Config.ALPHA_8)
    }

    @JvmStatic
    private fun getFullResIcon(iconId: Int): Drawable? {
        return ResourcesCompat.getDrawable(App.INSTANCE.resources, iconId, null)
    }


}