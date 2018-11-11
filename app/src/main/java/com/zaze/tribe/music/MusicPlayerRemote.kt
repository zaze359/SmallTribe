package com.zaze.tribe.music

import android.media.MediaPlayer
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.service.MusicService
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.util.*
import kotlin.collections.ArrayList

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-06 - 00:38
 */
object MusicPlayerRemote {

    /**
     * 播放列表
     */
    @JvmStatic
    val playerList = ObservableArrayList<MusicInfo>()

    /**
     * 当前播放 Music Data
     */
    @JvmStatic
    val curMusicData = ObservableField<MusicInfo>()

    /**
     * 是否在播放中
     */
    @JvmStatic
    val isPlaying = ObservableBoolean(false)

    /**
     * 循环模式 LoopMode
     */
    private val LOOP_MODE = ObservableInt(LoopMode.LOOP_LIST)

    // ------------------------------------------------------
    var mBinder: MusicService.ServiceBinder? = null
        set(value) {
            field = value
            field?.setPlayerCallback(object : MusicService.PlayerCallback {
                override fun preStart(musicInfo: MusicInfo, duration: Int) {
                    ZLog.i(ZTag.TAG_DEBUG, "preStart")
                    curMusicData.set(musicInfo)
                }

                override fun onStart(musicInfo: MusicInfo) {
                    ZLog.i(ZTag.TAG_DEBUG, "onStart : $musicInfo")
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

                override fun toNext() {
                    // 下一首
                    ZLog.i(ZTag.TAG_DEBUG, "toNext")
                    stop()
                    playAt(getNextPosition(true))
                }

                override fun onCompletion() {
                    ZLog.i(ZTag.TAG_DEBUG, "onCompletion")
                    playAt(getNextPosition(false))
                }

                override fun onError(mp: MediaPlayer?, what: Int, extra: Int) {
                    ZLog.i(ZTag.TAG_DEBUG, "onError")
                    stop()
                }
            })
        }

    // ------------------------------------------------------

    @JvmStatic
    fun resumePlaying() {
        if (!isPlaying.get()) {
            curMusicData.get()?.let {
                mBinder?.play(it)
            } ?: playAt(0)
        }

    }

    @JvmOverloads
    @JvmStatic
    fun play(musicInfo: MusicInfo? = curMusicData.get()) {
        musicInfo?.let {
            mBinder?.play(musicInfo)
        }
    }

    @Synchronized
    @JvmStatic
    fun playAt(position: Int) {
        if (position >= 0 && position < playerList.size && position != getCurPosition()) {
            stop()
            play(playerList[position])
        }
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
    fun seekTo(seekTimeMillis: Long) {
        mBinder?.seekTo(seekTimeMillis)
    }

    @Synchronized
    private fun getNextPosition(fromUser: Boolean): Int {
        var nextPosition = getCurPosition()
        if (nextPosition >= playerList.size - 1) {
            nextPosition = 0
        }
        when (LOOP_MODE.get()) {
            LoopMode.LOOP_LIST -> {
                nextPosition += 1
            }
            LoopMode.LOOP_RANDOM -> {
                nextPosition = Random().nextInt(playerList.size)
            }
            LoopMode.LOOP_SINGLE -> {
                if (!fromUser) {
                    nextPosition += 1
                }
            }
        }
        return nextPosition
    }

    @Synchronized
    fun addToPlayerList(musicInfo: MusicInfo): Int {
        val position = playerList.indexOf(musicInfo)
        if (position < 0) {
            playerList.add(musicInfo)
            return playerList.size - 1
        } else {
            return position
        }
    }

    @Synchronized
    fun addToPlayerList(musicList: List<MusicInfo>): List<MusicInfo> {
        musicList.forEach {
            addToPlayerList(it)
        }
        return playerList
    }

    @Synchronized
    @JvmStatic
    fun getCurPosition(): Int {
        return playerList.indexOf(curMusicData.get())
    }

    fun getProgress(): Int {
        return mBinder?.getCurProgress() ?: 0
    }

    fun getDuration(): Int {
        return mBinder?.getDuration() ?: 0
    }

    // --------------------------------------------------
    // --------------------------------------------------

    object LoopMode {
        const val LOOP_LIST = 0
        const val LOOP_SINGLE = 1
        const val SINGLE = 2
        const val LOOP_RANDOM = 3
    }
}