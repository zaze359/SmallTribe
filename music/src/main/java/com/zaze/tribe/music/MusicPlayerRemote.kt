package com.zaze.tribe.music

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.zaze.tribe.common.util.set
import com.zaze.tribe.music.data.dto.Music
import com.zaze.tribe.music.service.MusicService
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-06 - 00:38
 */
object MusicPlayerRemote {

    /**
     * 当前播放 Music Data
     */
    @JvmStatic
    val curMusicData = MutableLiveData<Music>()

    /**
     * 是否在播放中
     */
    @JvmStatic
    val isPlaying = ObservableBoolean(false)

    // ------------------------------------------------------
    var mBinder: MusicService.ServiceBinder? = null
        set(value) {
            field = value
            field?.setPlayerCallback(object : MusicService.PlayerCallback {

                override fun onStart(music: Music) {
                    ZLog.i(ZTag.TAG_DEBUG, "onStart : $music")
                    curMusicData.set(music)
                    isPlaying.set(true)
                }

                override fun onPause() {
                    ZLog.i(ZTag.TAG_DEBUG, "onPause")

                    isPlaying.set(false)
                }

                override fun onStop() {
                    ZLog.i(ZTag.TAG_DEBUG, "onStop")
                    isPlaying.set(false)
                }

                override fun onCompletion() {
                    ZLog.i(ZTag.TAG_DEBUG, "onCompletion")
                    isPlaying.set(false)
                }

                override fun onError(mp: MediaPlayer?, what: Int, extra: Int) {
                    ZLog.i(ZTag.TAG_DEBUG, "onError")
                    isPlaying.set(false)
                }
            })
        }

    // ------------------------------------------------------

    @JvmStatic
    fun bindService(context: Context, serviceConnection : ServiceConnection) {
        context.startService(Intent(context, MusicService::class.java))
        context.bindService(Intent(context, MusicService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
    }

    @JvmStatic
    fun unbindService(context: Context, serviceConnection : ServiceConnection) {
        context.unbindService(serviceConnection)
    }

    // ------------------------------------------------------

    @JvmStatic
    fun resumePlaying() {
        mBinder?.resume()
    }

    @JvmStatic
    fun playAt(position: Int) {
        mBinder?.playAt(position)
    }

    @JvmStatic
    fun playNext() {
        mBinder?.playNext()
    }

    @JvmStatic
    fun pause() {
        mBinder?.pause()
    }

    @JvmStatic
    fun stop() {
        mBinder?.stop()
    }

    @JvmStatic
    fun seekTo(seekTimeMillis: Int) {
        mBinder?.seekTo(seekTimeMillis)
    }


    @Synchronized
    fun addToPlayingQueue(music: Music): Int {
        return mBinder?.run {
            this.enqueue(music)
        } ?: -1
    }


    @Synchronized
    @JvmStatic
    fun getPosition(): Int {
        return mBinder?.run {
            this.getPosition()
        } ?: 0
    }

    fun getProgress(): Int {
        return mBinder?.getCurProgress() ?: 0
    }

    fun getDuration(): Int {
        return mBinder?.getDuration() ?: 0
    }

    fun getPlayingQueue(): ObservableArrayList<Music> {
        return mBinder?.getPlayingQueue() ?: ObservableArrayList()
    }

    // --------------------------------------------------
    // --------------------------------------------------

}