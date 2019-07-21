package com.zaze.tribe.reader.bookshelf

import android.app.Application
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import com.zaze.tribe.common.BaseAndroidViewModel
import com.zaze.tribe.common.plugins.rx.MyObserver
import com.zaze.tribe.common.util.set
import com.zaze.tribe.reader.bean.Book
import com.zaze.utils.FileUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
        Observable.create<List<File>> {
            it.onNext(FileUtil.searchFileBySuffix(Environment.getExternalStorageDirectory(), "txt", true))
            it.onComplete()
        }.subscribeOn(Schedulers.io()).map {
            it.mapTo(arrayListOf(), { file ->
                Book(file.name, file.absolutePath)
            })
        }.observeOn(AndroidSchedulers.mainThread())
                .doFinally{
                    dataLoading.set(false)
                }
                .subscribe(object : MyObserver<ArrayList<Book>>(compositeDisposable) {
                    override fun onNext(result: ArrayList<Book>) {
                        super.onNext(result)
                        bookData.set(result)
                    }
                })
    }

}