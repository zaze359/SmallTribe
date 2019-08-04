package com.zaze.tribe.reader

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zaze.tribe.common.BaseAndroidViewModel
import com.zaze.tribe.common.plugins.rx.MyObserver
import com.zaze.tribe.common.util.get
import com.zaze.tribe.common.util.set
import com.zaze.tribe.reader.bean.BookChapter
import com.zaze.tribe.reader.bean.ReaderBook
import com.zaze.tribe.reader.loader.BookLoader
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-20 - 18:52
 */
class ReaderViewModel(application: Application) : BaseAndroidViewModel(application) {

    val readerBookData = MutableLiveData<ReaderBook>()
    val catalogBookData = MutableLiveData<List<BookChapter>>()
    val curChapterIndex = MutableLiveData<Int>()

    fun loadFile(filePath: String) {
        Observable.fromCallable {
            BookLoader.loadBook(filePath)
        }.subscribeOn(Schedulers.io())
                .map {
                    readerBookData.set(ReaderBook(it))
                    catalogBookData.set(it.chapters)
                }
                .subscribe(MyObserver(compositeDisposable))
    }

    fun loadCatalog() {
        readerBookData.get()?.let {
            curChapterIndex.set(it.readerHistory.chapterIndex)
        }
    }
}