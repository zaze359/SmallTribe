package com.zaze.tribe.music.util

import android.graphics.Bitmap
import androidx.palette.graphics.Palette

/**
 * Description :
 * @author : zaze
 * @version : 2023-09-12 19:32
 */
object ColorUtil {
    fun generatePalette(bitmap: Bitmap?): Palette? {
        return if (bitmap == null) null else Palette.from(bitmap).clearFilters().generate()
    }
}