package com.zaze.tribe.reader.loader

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-21 - 22:50
 */
class BookLoader {

    companion object {
        const val TXT = ".txt"
    }

    fun loadBook(filePath: String) {
        val fileLoader: FileLoader = if(filePath.endsWith(TXT)) {
            TxtFileLoader()
        } else {
            TxtFileLoader()
        }
    }

}