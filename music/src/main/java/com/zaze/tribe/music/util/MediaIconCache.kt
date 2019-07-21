package com.zaze.tribe.music.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import com.zaze.tribe.common.util.IconCache
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
object MediaIconCache {

    private const val BMP_SIZE = 48

    private val mediaMetadataRetriever by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        MediaMetadataRetriever()
    }

    private var defaultMediaIcon: SoftReference<Bitmap>? = null

    @JvmStatic
    fun saveSmallMediaIcon(path: String, bmp: Bitmap?) {
        IconCache.saveIconCache(path, bmp)
    }

    @JvmStatic
    fun getSmallMediaIcon(path: String): Bitmap {
        return IconCache.getIconCache(path) ?: buildMediaIcon(path, BMP_SIZE, BMP_SIZE)?.apply {
            saveSmallMediaIcon(path, this)
        } ?: getDefaultMediaIcon()
    }

    @JvmStatic
    fun buildMediaIcon(path: String, width: Int = -1, height: Int = -1): Bitmap? {
        return try {
            mediaMetadataRetriever.run {
                setDataSource(path)
                embeddedPicture?.run {
                    BitmapFactory.decodeByteArray(this, 0, size, IconCache.buildBitmapOptions(width, height))
                }
            }
        } catch (e: Throwable) {
            ZLog.e(ZTag.TAG_DEBUG, "error media : $path")
            e.printStackTrace()
            null
        }
    }

    @JvmStatic
    fun getDefaultMediaIcon(): Bitmap {
        return defaultMediaIcon?.get()
                ?: BmpUtil.drawable2Bitmap(IconCache.getFullResIcon(R.mipmap.ic_launcher))
                        ?.apply {
                            defaultMediaIcon = SoftReference(this)
                        }
                ?: Bitmap.createBitmap(BMP_SIZE, BMP_SIZE, Bitmap.Config.ALPHA_8)
    }
}