package com.zaze.tribe.reader

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zaze.tribe.common.BaseAndroidViewModel
import com.zaze.tribe.common.plugins.rx.MyObserver
import com.zaze.tribe.common.util.get
import com.zaze.tribe.common.util.set
import com.zaze.tribe.reader.bean.Book
import com.zaze.tribe.reader.bean.ReaderPage
import com.zaze.tribe.reader.loader.BookLoader
import com.zaze.tribe.reader.util.ReaderProfile
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

    var book: Book? = null
    val readerPageData = MutableLiveData<ReaderPage>()


    fun loadFile(filePath: String) {
        Observable.fromCallable {
            book = BookLoader.loadBook(filePath)
        }.subscribeOn(Schedulers.io())
                .map {
                    book?.let {
                        if (it.chapters.size > 0) {
                            // 加载之前保存的页面数据
                            val readerPage = readerPageData.get() ?: ReaderPage(it.chapters[0])
                            for (chapter in it.chapters) {
                                readerPage.chapter = chapter
                                if (chapter.paragraphEndIndex >= readerPage.currentParagraphIndex) {
                                    break
                                }
                            }
                            if (it.paragraphs.size >= readerPage.chapter.paragraphEndIndex) {
                                readerPage.maxParagraphs.clear()
                                readerPage.maxParagraphs.addAll(it.paragraphs.subList(
                                        readerPage.currentParagraphIndex,
                                        readerPage.chapter.paragraphEndIndex
                                ))
                            }
                            readerPageData.set(readerPage)
                        }
                    }
                }
                .subscribe(MyObserver(compositeDisposable))
    }
}