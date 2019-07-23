package com.zaze.tribe.reader.bean

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-23 - 22:12
 */
class ReaderPage(var chapter: BookChapter) {
    var currentParagraphIndex: Int = 0
    var maxParagraphs = ArrayList<BookParagraph>()
}