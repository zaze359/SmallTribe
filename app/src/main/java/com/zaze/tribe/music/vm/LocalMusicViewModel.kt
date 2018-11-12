package com.zaze.tribe.music.vm

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableList
import androidx.lifecycle.AndroidViewModel
import com.zaze.tribe.data.dto.Music
import com.zaze.tribe.data.loaders.MusicLoader
import com.zaze.tribe.data.source.repository.MusicRepository
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
class LocalMusicViewModel(
        private val context: Application,
        private val musicRepository: MusicRepository
) : AndroidViewModel(context) {
    val musicList: ObservableList<Music> = ObservableArrayList()
    /**
     * 是否加载中
     */
    val dataLoading = ObservableBoolean(false)

    private val compositeDisposable = CompositeDisposable()

    // ------------------------------------------------------
    // ------------------------------------------------------

    fun loadMusics() {
        dataLoading.set(true)
        musicRepository.getMusicInfoList()
                .map {
                    if (!it.isEmpty()) {
                        MusicPlayerRemote.addToPlayerList(it)
                    }
                }.map {
                    MusicLoader.getAllMusics(context).let { musics ->
                        musicList.apply {
                            clear()
                            addAll(musics)
                        }
                    }
                }.subscribe(object : Subscriber<List<Music>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(s: Subscription?) {
                        s?.request(1)
                    }

                    override fun onNext(t: List<Music>?) {
                        dataLoading.set(false)
                    }

                    override fun onError(t: Throwable?) {
                        t?.printStackTrace()
                        dataLoading.set(false)
                    }
                })
    }

    fun start(music: Music?) {
        music?.apply {
            compositeDisposable.add(
                    Observable.create<Music> { emitter ->
                        emitter.onNext(this)
                        emitter.onComplete()
                    }.subscribeOn(Schedulers.io())
                            .map { it ->
                                musicRepository.saveMusicInfo(music)
                                MusicPlayerRemote.addToPlayerList(it)
                            }.observeOn(AndroidSchedulers.mainThread())
                            .map { position ->
                                MusicPlayerRemote.playAt(position)
                            }.subscribe()
            )
        }
    }
    // ------------------------------------------------------
    /**
     * 显示更多
     * [music] music
     */
    fun showMore(music: Music) {
        ZTipUtil.toast(context, music.data)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}