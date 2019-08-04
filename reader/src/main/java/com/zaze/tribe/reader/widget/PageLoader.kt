package com.zaze.tribe.reader.widget

import com.zaze.tribe.reader.bean.ReaderPage

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-07-26 - 15:01
 */
interface PageLoader : PageParser {

    fun loadNextPage()
    fun loadPrePage()

    fun loadNextChapter()
    fun loadPreChapter()
    fun onLoaded(readerPage: ReaderPage)
}