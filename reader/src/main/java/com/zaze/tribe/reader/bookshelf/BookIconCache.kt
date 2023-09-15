package com.zaze.tribe.reader.bookshelf

import android.graphics.*
import com.zaze.tribe.common.util.IconCache
import com.zaze.tribe.reader.bean.Book
import com.zaze.utils.DisplayUtil


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
        textSize = DisplayUtil.pxFromDp(18F)
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
            val lineSize = (width / textPaint.textSize).toInt()
            sCanvas.setBitmap(bitmap)
            sCanvas.drawColor(Color.WHITE)
            var offsetX = 0F
            for (i in 0..name.length / lineSize) {
//                if(height - width <= textPaint.textSize) {
//                    break
//                }
                sCanvas.drawText(name.substring(i * lineSize, (i + 1) * lineSize), offsetX, textPaint.textSize, textPaint)
                offsetX += textPaint.textSize + 2
            }
            // 剩余字数
            sCanvas.drawText(name.substring(name.length - name.length % lineSize, name.length), offsetX, textPaint.textSize, textPaint)
            sCanvas.setBitmap(null)
            return bitmap
        }
    }

}