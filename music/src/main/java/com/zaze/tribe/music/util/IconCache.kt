package com.zaze.tribe.music.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.util.LruCache
import androidx.core.content.res.ResourcesCompat
import com.zaze.tribe.common.BaseApplication
import com.zaze.tribe.music.R
import com.zaze.utils.BmpUtil
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

//    @JvmStatic
//    fun getSmallMediaIcon(path: String): Bitmap {
//        return if (FileUtil.exists(path)) {
//            BITMAP_CACHE.get(path) ?: buildMediaIcon(path, BMP_SIZE, BMP_SIZE)?.apply {
//                saveSmallMediaIcon(path, this)
//            } ?: getDefaultMediaIcon()
//        } else {
//            getDefaultMediaIcon()
//        }
//    }

    @JvmStatic
    fun getSmallMediaIcon(path: String): Bitmap {
        return BITMAP_CACHE.get(path) ?: buildMediaIcon(path, BMP_SIZE, BMP_SIZE)?.apply {
            saveSmallMediaIcon(path, this)
        } ?: getDefaultMediaIcon()
    }

    @JvmStatic
    fun buildMediaIcon(path: String, width: Int = -1, height: Int = -1): Bitmap? {
        try {
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
                        if (width > 0 && height > 0) {
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
                        }
                    })
                }
            }
        } catch (e: Throwable) {
            ZLog.e(ZTag.TAG_DEBUG, "error media : $path")
            e.printStackTrace()
            return null
        }
    }

    @JvmStatic
    fun getDefaultMediaIcon(): Bitmap {
        return defaultMediaIcon?.get()
                ?: BmpUtil.drawable2Bitmap(getFullResIcon(R.mipmap.ic_launcher))
                        ?.apply {
                            defaultMediaIcon = SoftReference(this)
                        }
                ?: Bitmap.createBitmap(BMP_SIZE, BMP_SIZE, Bitmap.Config.ALPHA_8)
    }

    @JvmStatic
    private fun getFullResIcon(iconId: Int): Drawable? {
        return ResourcesCompat.getDrawable(BaseApplication.INSTANCE.resources, iconId, null)
    }


}