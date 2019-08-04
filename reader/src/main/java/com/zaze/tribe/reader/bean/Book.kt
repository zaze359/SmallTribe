package com.zaze.tribe.reader.bean

import java.io.File

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-06-08 - 0:43
 */
class Book(file: File) {
    val name: String = file.name.split(".")[0]
    val localPath: String = file.absolutePath

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