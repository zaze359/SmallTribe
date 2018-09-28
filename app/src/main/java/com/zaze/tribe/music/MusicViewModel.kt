package com.zaze.tribe.music

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.databinding.*
import android.media.MediaPlayer
import android.os.IBinder
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.data.source.repository.MusicRepository
import com.zaze.tribe.service.PlayerService
import com.zaze.utils.ZTipUtil
import com.zaze.utils.log.LogcatUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.*

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-06 - 00:38
 */
class MusicViewModel(
        private val context: Application,
        private val musicRepository: MusicRepository
) : AndroidViewModel(context) {
    val musicList: ObservableList<MusicInfo> = ObservableArrayList()
    /**
     * 是否加载中
     */
    val dataLoading = ObservableBoolean(false)

    val progress = ObservableInt(0)
    /**
     * 仅用于UI的显示，不负责逻辑判断
     */
    val isPaused = ObservableBoolean(true)

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            ZLog.e(ZTag.TAG_DEBUG, "onServiceDisconnected : $name")
            MusicPlayerRemote.mBinder = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            ZLog.i(ZTag.TAG_DEBUG, "onServiceConnected : $name")
            MusicPlayerRemote.mBinder = service as PlayerService.ServiceBinder
        }
    }

    // ------------------------------------------------------
    // ------------------------------------------------------

    fun loadMusics() {
        dataLoading.set(true)
        var subscription: Subscription? = null
        musicRepository.getMusicInfoList()
                .map {
                    if (it.isEmpty()) {
                        //  TODO 先简单的扫描一遍
                        MusicPlayerRemote.scanMusicFromSd()
                    } else {
                        it
                    }
                }.map {
                    musicList.apply {
                        clear()
                        addAll(it)
                    }
                }.doFinally {
                    dataLoading.set(false)
                }.subscribe(object : Subscriber<ObservableList<MusicInfo>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(s: Subscription?) {
                        subscription = s
                        subscription?.request(1)
                    }

                    override fun onNext(t: ObservableList<MusicInfo>?) {
                        dataLoading.set(false)
                    }

                    override fun onError(t: Throwable?) {
                        t?.printStackTrace()
                        dataLoading.set(false)
                    }
                })
    }

    /**
     * 显示更多
     * [music] music
     */
    fun showMore(music: MusicInfo) {
        ZTipUtil.toast(context, music.localPath)
    }

    /**
     * 开始播放
     * [musicInfo] music
     */
    fun start(musicInfo: MusicInfo? = null) {
        MusicPlayerRemote.start(musicInfo)
    }

    fun pause() {
        MusicPlayerRemote.pause()
    }

    fun stop() {
        MusicPlayerRemote.stop()
    }

    fun seekTo(musicInfo: MusicInfo, seekTimeMillis: Long) {
        MusicPlayerRemote.seekTo(musicInfo, seekTimeMillis)
    }

    fun bindService() {
        context.bindService(Intent(context, PlayerService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun unbindService() {
        context.unbindService(serviceConnection)
    }
}