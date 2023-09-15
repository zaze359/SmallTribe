package com.zaze.tribe.reader.data.repository

import com.zaze.tribe.common.di.CustomDispatchers
import com.zaze.tribe.common.di.Dispatcher
import com.zaze.tribe.reader.bean.Book
import com.zaze.tribe.reader.data.loader.BookLoader
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookRepository @Inject constructor(@Dispatcher(CustomDispatchers.IO) private val dispatcher: CoroutineDispatcher) {

    suspend fun loadBook(filePath: String): Book = withContext(dispatcher) {
        ZLog.i(ZTag.TAG_DEBUG, "inner ${Thread.currentThread().name}")
        BookLoader.loadBook(filePath)
    }
}