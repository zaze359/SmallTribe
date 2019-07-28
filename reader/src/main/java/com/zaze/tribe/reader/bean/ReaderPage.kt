package com.zaze.tribe.reader.bean

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-23 - 22:12
 */
class ReaderPage @JvmOverloads constructor(var chapter: String = "") {
    var paragraphIndex = 0
    var charIndex = 0
    val lines = ArrayList<BookLine>()
}