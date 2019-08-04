package com.zaze.tribe.reader.loader

import com.zaze.tribe.reader.bean.Book
import com.zaze.tribe.reader.util.FileCharset
import java.io.File


/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-21 - 22:50
 */
object BookLoader {
    private const val TXT = ".txt"

    /**
     * 加载书
     */
    fun loadBook(filePath: String): Book {
        val book = Book(File(filePath))
        val fileLoader: FileLoader = if (filePath.endsWith(TXT)) {
            TxtFileLoader()
        } else {
            TxtFileLoader()
        }
        fileLoader.loadFileIntoBook(book, FileCharset.getCharset(filePath))
        return book
    }

}