package com.zaze.tribe.reader.loader

import com.zaze.tribe.reader.bean.BookChapter
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.regex.Pattern

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-21 - 22:49
 */
class TxtFileLoader : FileLoader {

    companion object {
        val pattern = Pattern.compile("")
    }

    override fun loadFile(filePath: String): List<BookChapter> {
        val chapters = ArrayList<BookChapter>()
        File(filePath).apply {
            val bufferedReader = BufferedReader(InputStreamReader(FileInputStream(this)))
            var line: String? = bufferedReader.readLine()
            var chapter: BookChapter
            while (line != null) {
                if (line.isEmpty()) {
                    matchChapter(line)?.let {
                        chapter = it
                    }
                }
                line = bufferedReader.readLine()
            }
        }
        return chapters
    }

    fun matchChapter(line: String): BookChapter? {
        val matcher = pattern.matcher(line)
        return if (matcher.find()) {
            BookChapter()
        } else {
            null
        }
    }
}