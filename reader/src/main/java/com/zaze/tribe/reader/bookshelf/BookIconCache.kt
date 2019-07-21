package com.zaze.tribe.reader.bookshelf

import android.graphics.*
import com.zaze.utils.ZDisplayUtil
import com.zaze.tribe.common.util.IconCache
import com.zaze.tribe.reader.bean.Book


/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-06-08 - 21:13
 */
object BookIconCache {
    private val sCanvas = Canvas()
    private const val width = 192
    private const val height = 240

    private val textPaint = Paint().apply {
        color = Color.parseColor("#333333")
        isAntiAlias = true
        isDither = true
        textSize = ZDisplayUtil.pxFromDp(18F)
        strokeWidth = 1F
    }

    @JvmStatic
    fun getBookCover(book: Book): Bitmap {
        return IconCache.getIconCache(book.name) ?: createDefaultCover(book.name).apply {
            IconCache.saveIconCache(book.name, this)
        }
    }

    @JvmStatic
    private fun createDefaultCover(name: String): Bitmap {
        synchronized(sCanvas) {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            sCanvas.setBitmap(bitmap)
            sCanvas.drawColor(Color.WHITE)
            sCanvas.drawText(name, 0F, textPaint.textSize, textPaint)
            sCanvas.setBitmap(null)
            return bitmap
        }
    }

}