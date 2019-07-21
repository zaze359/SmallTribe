package com.zaze.tribe.common.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.LruCache
import androidx.core.content.res.ResourcesCompat
import com.zaze.tribe.common.BaseApplication
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-17 - 09:54
 */
object IconCache {

    private val BITMAP_CACHE = LruCache<String, Bitmap>(40)

    @JvmStatic
    fun saveIconCache(key: String, bmp: Bitmap?) {
        bmp?.let {
            BITMAP_CACHE.put(key, bmp)
        }
    }

    @JvmStatic
    fun getIconCache(key: String): Bitmap? {
        return BITMAP_CACHE.get(key)
    }


    @JvmStatic
    fun getFullResIcon(iconId: Int): Drawable? {
        return ResourcesCompat.getDrawable(BaseApplication.INSTANCE.resources, iconId, null)
    }


    @JvmStatic
    fun buildBitmapOptions(width: Int = -1, height: Int = -1): BitmapFactory.Options {
        return BitmapFactory.Options().apply {
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
        }
    }
}