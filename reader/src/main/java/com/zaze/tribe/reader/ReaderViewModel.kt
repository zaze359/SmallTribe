package com.zaze.tribe.reader

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.zaze.tribe.common.BaseAndroidViewModel
import com.zaze.tribe.common.util.set
import com.zaze.tribe.reader.bean.BookChapter
import com.zaze.tribe.reader.bean.ReaderBook
import com.zaze.tribe.reader.data.repository.BookRepository
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-20 - 18:52
 */
class ReaderViewModel(application: Application, private val repository: BookRepository) : BaseAndroidViewModel(application) {

    val readerBookData = MutableLiveData<ReaderBook>()
    val catalogBookData = MutableLiveData<List<BookChapter>>()
    val curChapterIndex = MutableLiveData<Int>()

    fun loadFile(filePath: String) {
        viewModelScope.launch {
            val book = repository.loadBook(filePath)
            readerBookData.value = ReaderBook(book)
            catalogBookData.value = book.chapters
        }
    }

    fun showPointChapter(chapterIndex: Int) {
        curChapterIndex.set(chapterIndex)
    }
}

object ReaderViewModelFactory {

    fun provideFactory(application: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ReaderViewModel(application, BookRepository(Dispatchers.IO)) as T
        }
    }
}
