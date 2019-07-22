package com.zaze.tribe.reader.bean

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-06-08 - 0:43
 */
class Book(val name: String, val localPath: String) {

    var chapters = ArrayList<BookChapter>()

    var paragraphs = ArrayList<BookParagraph>()
}