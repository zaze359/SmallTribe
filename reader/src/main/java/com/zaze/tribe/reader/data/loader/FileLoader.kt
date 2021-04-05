package com.zaze.tribe.reader.data.loader

import com.zaze.tribe.reader.bean.Book

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-21 - 22:52
 */
interface FileLoader {

    fun loadFileIntoBook(book: Book, charset: String)
}