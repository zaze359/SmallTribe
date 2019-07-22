package com.zaze.tribe.reader

import android.app.Application
import com.zaze.tribe.common.BaseAndroidViewModel
import com.zaze.tribe.common.plugins.rx.MyObserver
import com.zaze.tribe.reader.loader.BookLoader
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-20 - 18:52
 */
class ReaderViewModel(application: Application) : BaseAndroidViewModel(application) {

    fun loadFile(filePath: String) {
        Observable.fromCallable {
            BookLoader.loadBook(filePath)
        }.subscribeOn(Schedulers.io())
                .subscribe(MyObserver(compositeDisposable))
    }
}