package com.zaze.tribe.reader

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zaze.tribe.common.base.AbsAndroidViewModel
import com.zaze.tribe.common.util.set
import com.zaze.tribe.reader.bean.BookChapter
import com.zaze.tribe.reader.bean.ReaderBook
import com.zaze.tribe.reader.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-20 - 18:52
 */
@HiltViewModel
class ReaderViewModel @Inject constructor(
    application: Application,
    private val repository: BookRepository
) : AbsAndroidViewModel(application) {

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