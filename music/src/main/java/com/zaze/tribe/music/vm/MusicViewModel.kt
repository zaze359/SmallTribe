package com.zaze.tribe.music.vm

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.zaze.tribe.music.data.dto.Music
import com.zaze.tribe.music.data.loaders.MusicLoader
import com.zaze.tribe.music.data.source.repository.MusicRepository
import com.zaze.tribe.music.MusicPlayerRemote
import com.zaze.utils.ZTipUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-06 - 00:38
 */
open class MusicViewModel(
        private val context: Application
) : AndroidViewModel(context) {
    val musicList: ObservableList<Music> = ObservableArrayList()
    /**
     * 是否加载中
     */
    val dataLoading = ObservableBoolean(false)

    val isFavorite = MutableLiveData<Boolean>()


    private val compositeDisposable = CompositeDisposable()

    // ------------------------------------------------------
    // ------------------------------------------------------

    fun loadMusics() {
        dataLoading.set(true)
        compositeDisposable.add(
                Observable.create<List<Music>> {
                    it.onNext(MusicLoader.getLocalMusics(context))
                    it.onComplete()
                }.subscribeOn(Schedulers.io())
                        .map { musics ->
                            musicList.apply {
                                clear()
                                addAll(musics)
                            }
                        }.doFinally {
                            dataLoading.set(false)
                        }.subscribe()
        )
    }

    fun addAndPlay(music: Music) {
        compositeDisposable.add(
                Observable.create<Music> { emitter ->
                    emitter.onNext(music)
                    emitter.onComplete()
                }.subscribeOn(Schedulers.io())
                        .map { it ->
                            MusicPlayerRemote.addToPlayingQueue(it)
                        }.observeOn(AndroidSchedulers.mainThread())
                        .map { position ->
                            MusicPlayerRemote.playAt(position)
                        }.subscribe()
        )
    }
    // ------------------------------------------------------
    /**
     * 显示更多
     * [music] music
     */
    fun showMore(music: Music) {
        ZTipUtil.toast(context, music.data)
    }

    fun toggleFavorite(music: Music?) {

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}