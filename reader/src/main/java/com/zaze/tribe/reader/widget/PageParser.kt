package com.zaze.tribe.reader.widget

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-07-26 - 15:01
 */
interface PageParser {

    fun measureTextWidth(chars: CharArray): Int

    fun maxLines(): Int
}