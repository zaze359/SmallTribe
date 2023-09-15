package com.zaze.tribe.reader.bookshelf

import android.app.Application
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zaze.tribe.common.base.AbsAndroidViewModel
import com.zaze.tribe.common.util.Utils
import com.zaze.tribe.common.util.set
import com.zaze.tribe.reader.bean.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-06-08 - 0:51
 */
class BookshelfViewModel(application: Application) : AbsAndroidViewModel(application) {

    val bookData = MutableLiveData<List<Book>?>()

    fun loadBookshelf() {
        dataLoading.set(true)
        viewModelScope.launch() {
            bookData.value = withContext(Dispatchers.IO) {
                Utils.searchFileBySuffix(Environment.getExternalStorageDirectory(), "txt", true)
                    .map {
                        Book(it)
                    }
            }
            dataLoading.set(false)
        }
    }
}