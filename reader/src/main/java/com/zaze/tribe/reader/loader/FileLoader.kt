package com.zaze.tribe.reader.loader

import com.zaze.tribe.reader.bean.BookChapter

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-21 - 22:52
 */
interface FileLoader {
    fun loadFile(filePath: String): List<BookChapter>
}