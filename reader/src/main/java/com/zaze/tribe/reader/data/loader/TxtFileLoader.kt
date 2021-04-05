package com.zaze.tribe.reader.data.loader

import com.zaze.tribe.reader.bean.Book
import com.zaze.tribe.reader.bean.BookChapter
import com.zaze.tribe.reader.bean.BookParagraph
import com.zaze.utils.log.ZLog
import java.io.*
import java.util.regex.Pattern

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-21 - 22:49
 */
class TxtFileLoader : FileLoader {

    companion object {
        /**
         * 1. 以任意个空格或者非下划线符号为开头
         * 2. 包含 第
         * 3. 0-9 一-九 十千百万
         * 4. 章部节卷集级片篇回
         * 5 后面什么都没 或者 存在至少一个空格或者非下划线符号
         * 6. 至多20个字的章节描述
         */
        val pattern = Pattern.compile("(^([\\W]*)第[0-9\\u4e00-\\u9fa5\\u767e\\u5343\\u96f6]{1,10}[章部节卷集片篇回])([\\W]+(.{0,20}))?$")
    }

    override fun loadFileIntoBook(book: Book, charset: String) {
        File(book.localPath).apply {
            var bufferedReader : BufferedReader? = null
            try {
                bufferedReader = BufferedReader(InputStreamReader(FileInputStream(this), charset))
                var paragraphIndex = 0
                val chapter = BookChapter("开始").apply {
                    this.paragraphStartIndex = paragraphIndex
                }
                //
                var line: String? = bufferedReader.readLine()
                var charIndex = 0
                while (line != null) {
                    if (line.isNotEmpty()) {
                        matchChapter(line)?.let {
                            chapter.paragraphEndIndex = paragraphIndex
                            book.chapters.add(chapter.fork())
                            chapter.reset(it, paragraphIndex)
                        }
                        book.paragraphs.add(BookParagraph(charIndex, line))
                        charIndex += line.length
                        paragraphIndex++
                    }
                    line = bufferedReader.readLine()
                }
                chapter.paragraphEndIndex = paragraphIndex
                book.chapters.add(chapter)
            } catch (e : Exception) {
                e.printStackTrace()
            } finally {
                try {
                    bufferedReader?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun matchChapter(line: String): String? {
        val matcher = pattern.matcher(line)
        return if (matcher.find()) {
            ZLog.i("TxtFileLoader 0", matcher.group(0))
//            ZLog.i("TxtFileLoader 1", matcher.group(1))
//            ZLog.i("TxtFileLoader 2", matcher.group(2))
//            ZLog.i("TxtFileLoader 3", matcher.group(3))
//            ZLog.i("TxtFileLoader 4", matcher.group(4))
            line
        } else {
            null
        }
    }
}