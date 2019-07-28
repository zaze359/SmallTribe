package com.zaze.tribe.reader.bean

/**
 * Description : 段落
 *
 * @author : ZAZE
 * @version : 2019-07-20 - 20:41
 */
class BookParagraph(var startCharIndex: Int, var paragraph: String) {
    val endCharIndex: Int
        get() {
            return startCharIndex + paragraph.length
        }

    fun substring(start: Int): BookParagraph {
        return BookParagraph(startCharIndex + start, paragraph.substring(start))
    }
}