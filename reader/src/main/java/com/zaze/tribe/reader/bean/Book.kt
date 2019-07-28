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


    fun isLastChapter(chapterIndex: Int): Boolean {
        return chapterIndex >= chapters.size - 1
    }

    fun isFirstChapter(chapterIndex: Int): Boolean {
        return chapterIndex <= 0
    }

    fun subParagraphs(start: Int, end: Int): List<BookParagraph> {
        return if (start < paragraphs.size) {
            paragraphs.subList(start, Math.min(end, paragraphs.size - 1))
        } else {
            ArrayList()
        }
    }
}