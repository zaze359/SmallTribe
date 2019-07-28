package com.zaze.tribe.reader.bean

import com.zaze.tribe.reader.widget.PageLoader
import com.zaze.tribe.reader.widget.PageParser
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-06-08 - 0:43
 */
class ReaderBook(var book: Book) {

    var readerHistory = ReaderHistory()
    /**
     *
     */
    private val readerPages = ArrayList<ReaderPage>()
    /**
     *
     */
    private var currPageIndex = 0


    /**
     * 加载历史页
     */
    fun loadFromHistory(pageLoader: PageLoader) {
        load(pageLoader, readerHistory.chapterIndex)
    }

    /**
     * 加载
     */
    private fun load(pageLoader: PageLoader, chapterIndex: Int, reverse: Boolean = false) {
        loadPagesFormChapter(pageLoader, chapterIndex)
        if (readerPages.isNotEmpty()) {
            pageLoader.onLoaded(if (reverse) {
                readerPages[readerPages.size - 1]
            } else {
                readerPages[0]
            })
        }
    }

    /**
     * 加载下一页
     */
    fun loadNextPage(pageLoader: PageLoader) {
        if (currPageIndex >= readerPages.size - 1) {
            loadNextChapter(pageLoader)
        } else {
            currPageIndex++
            val readerPage = readerPages[currPageIndex]
            readerHistory.nextPage(readerPage)
            pageLoader.onLoaded(readerPage)
        }
    }

    /**
     * 加载上一页
     */
    fun loadPrePage(pageLoader: PageLoader) {
        if (currPageIndex <= 0) {
            loadPreChapter(pageLoader, true)
        } else {
            currPageIndex--
            val readerPage = readerPages[currPageIndex]
            readerHistory.prePage(readerPage)
            pageLoader.onLoaded(readerPage)
        }
    }

    /**
     * 加载下一章
     */
    fun loadNextChapter(pageLoader: PageLoader) {
        if (book.isLastChapter(readerHistory.chapterIndex)) {
            ZLog.i("ReaderBook", "当前为最后一章")
            return
        }
        currPageIndex = 0
        ZLog.i("ReaderBook", "加载下一章节")
        readerHistory.nextChapter()
        load(pageLoader, readerHistory.chapterIndex)
    }

    /**
     * 加载上一章
     * [pageLoader] pageLoader
     * [reverse] 是否需要反转显示 默认false 从第一页显示
     */
    fun loadPreChapter(pageLoader: PageLoader, reverse: Boolean = false) {
        if (book.isFirstChapter(readerHistory.chapterIndex)) {
            ZLog.i("ReaderBook", "当前为第一章")
            return
        }
        currPageIndex = 0
        ZLog.i("ReaderBook", "加载上一章节")
        readerHistory.preChapter()
        load(pageLoader, readerHistory.chapterIndex, reverse)
    }

    /**
     * 从章节数据中加载页面
     */
    private fun loadPagesFormChapter(parser: PageParser, chapterIndex: Int) {
        readerPages.clear()
        if (book.chapters.size > chapterIndex) {
            // 获取到历史阅读章节
            val chapter = book.chapters[chapterIndex]
            val paragraphs = book.subParagraphs(chapter.paragraphStartIndex, chapter.paragraphEndIndex)
            var readerPage = ReaderPage()
            for ((paragraphIndex, paragraph) in paragraphs.withIndex()) {
                ZLog.d(ZTag.TAG_DEBUG, paragraph.paragraph)
                var readCharLength = 0
                var chars = paragraph.paragraph.toCharArray()
                while (chars.isNotEmpty()) {
                    val charLength = parser.measureTextWidth(chars)
                    readCharLength += charLength
                    readerPage.lines.add(BookLine(String(chars.copyOfRange(0, charLength))))
                    chars = chars.copyOfRange(charLength, chars.size)
                    if (readerPage.lines.size == parser.maxLines()) {
                        // 记录段落位置
                        readerPages.add(readerPage)
                        readerPage = ReaderPage()
                        readerPage.paragraphIndex = paragraphIndex
                        readerPage.charIndex = readCharLength
                    }
                }
            }
            if (readerPage.lines.isNotEmpty()) {
                readerPages.add(readerPage)
            }
        }
    }
}