package com.zaze.tribe.reader.loader

import com.zaze.tribe.reader.bean.Book
import com.zaze.tribe.reader.bean.BookChapter
import java.io.File

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-21 - 22:52
 */
interface FileLoader {

    fun loadFileIntoBook(book: Book, charset: String)
}