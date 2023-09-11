package com.zaze.tribe.reader.bookshelf

import android.app.Application
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zaze.tribe.common.BaseAndroidViewModel
import com.zaze.tribe.common.plugins.rx.MyObserver
import com.zaze.tribe.common.util.Utils
import com.zaze.tribe.common.util.set
import com.zaze.tribe.reader.bean.Book
import com.zaze.utils.FileUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
class BookshelfViewModel(application: Application) : BaseAndroidViewModel(application) {

    val bookData = MutableLiveData<List<Book>>()

    fun loadBookshelf() {
        dataLoading.set(true)
        viewModelScope.launch {
            bookData.value = withContext(Dispatchers.IO) {
                Utils.searchFileBySuffix(Environment.getExternalStorageDirectory(), "txt", true)
                        .mapTo(arrayListOf()) { file ->
                            Book(file)
                        }
            }
            dataLoading.set(false)
        }
    }
}