package com.zaze.tribe.music

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.*
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.data.loaders.MusicLoader
import com.zaze.tribe.data.source.repository.MusicRepository
import com.zaze.utils.ZTipUtil
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-06 - 00:38
 */
class MusicListViewModel(
        private val context: Application,
        private val musicRepository: MusicRepository
) : AndroidViewModel(context) {
    val musicList: ObservableList<MusicInfo> = ObservableArrayList()
    /**
     * 是否加载中
     */
    val dataLoading = ObservableBoolean(false)

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
                }.subscribe(object : Subscriber<List<MusicInfo>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(s: Subscription?) {
                        s?.request(1)
                    }

                    override fun onNext(t: List<MusicInfo>?) {
                        dataLoading.set(false)
                    }

                    override fun onError(t: Throwable?) {
                        t?.printStackTrace()
                        dataLoading.set(false)
                    }
                })
    }

    fun start(music: MusicInfo) {
        Observable.create<MusicInfo> { emitter ->
            emitter.onNext(music)
            emitter.onComplete()
        }.subscribeOn(Schedulers.io()).map {
            MusicPlayerRemote.addToPlayerList(listOf(it))
        }.map {
            if (!it.isEmpty()) {
                musicRepository.saveMusicInfo(music)
            }
        }.map {
            MusicPlayerRemote.start(music)
        }.subscribe()
    }
    // ------------------------------------------------------
    /**
     * 显示更多
     * [music] music
     */
    fun showMore(music: MusicInfo) {
        ZTipUtil.toast(context, music.data)
    }
}