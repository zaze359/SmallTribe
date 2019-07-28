package com.zaze.tribe.reader.bean

/**
 * Description : 阅读历史
 * @author : ZAZE
 * @version : 2019-07-24 - 20:43
 */
class ReaderHistory {

    var bookId = 0
    /**
     * 第几章
     */
    var chapterIndex = 0
    /**
     * 章节中的段落位置
     */
    var paragraphIndex = 0
    /**
     * 段落中的字符位置
     */
    var charIndex = 0

    /**
     * 进度
     */
    var progress = 0.0F

    fun nextChapter() {
        chapterIndex++
        paragraphIndex = 0
        charIndex = 0
    }

    fun preChapter() {
        chapterIndex = Math.max(0, chapterIndex - 1)
        paragraphIndex = 0
        charIndex = 0
    }

    fun nextPage(readerPage: ReaderPage) {
        paragraphIndex = readerPage.paragraphIndex
        charIndex = readerPage.charIndex
    }

    fun prePage(readerPage: ReaderPage) {
        paragraphIndex = readerPage.paragraphIndex
        charIndex = readerPage.charIndex
    }
}