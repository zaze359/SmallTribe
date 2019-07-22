package com.zaze.tribe.reader.bean

/**
 * Description : 章节
 *
 * @author : ZAZE
 * @version : 2019-07-20 - 19:56
 */
data class BookChapter(var chapter: String) {

    /**
     * 第几段落开始
     */
    var paragraphStartIndex: Int = 0
    /**
     * 第几段结束
     */
    var paragraphEndIndex: Int = paragraphStartIndex

    fun reset(chapter: String, paragraphStartIndex: Int) {
        this.chapter = chapter
        this.paragraphStartIndex = paragraphStartIndex
    }

    fun fork(): BookChapter {
        return BookChapter(chapter).also {
            it.paragraphStartIndex = paragraphStartIndex
            it.paragraphEndIndex = paragraphEndIndex
        }
    }

}