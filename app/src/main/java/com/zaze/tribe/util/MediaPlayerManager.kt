package com.zaze.tribe.util

import android.app.Application
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-07-23 - 18:58
 */
class MediaPlayerManager private constructor(private val context: Application) {
    private var mediaPlayer: MediaPlayer? = null
    private var startTimeMillis = 0L
    private var pauseTimeMillis = 0L
    val curMusicData = ObservableField<MusicInfo>()
    val progress = ObservableInt()

    private val errorListener = MediaPlayer.OnErrorListener { mp, what, extra ->
        true
    }

    companion object {
        @Volatile
        private var INSTANCE: MediaPlayerManager? = null

        fun getInstance(context: Application) = INSTANCE
                ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: MediaPlayerManager(context).also {
                        INSTANCE = it
                    }
                }
    }

    @Synchronized
    fun start(musicInfo: MusicInfo) {
        mediaPlayer ?: MediaPlayer.create(context, Uri.parse("file://${musicInfo.localPath}")).let {
            mediaPlayer = it
            it.setOnErrorListener(errorListener)
        }
        mediaPlayer?.apply {
            if (isPlaying) {
                val curMusic = curMusicData.get()
                if (curMusic != null && musicInfo.localPath == curMusic.localPath) {
                    this@MediaPlayerManager.pause()
                } else {
                    this@MediaPlayerManager.stop()
                    this@MediaPlayerManager.start(musicInfo)
                }
            } else {
                curMusicData.set(musicInfo)
                ZLog.i(ZTag.TAG_DEBUG, "startPlaying : $musicInfo")
                start()
                startTimeMillis = System.currentTimeMillis()
            }
        }
    }

    @Synchronized
    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                ZLog.i(ZTag.TAG_DEBUG, "pausePlaying : ${curMusicData.get()}")
                it.pause()
                pauseTimeMillis = System.currentTimeMillis()
            }
        }
    }

    @Synchronized
    fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                ZLog.i(ZTag.TAG_DEBUG, "stopPlaying : ${curMusicData.get()}")
                it.stop()
                it.release()
                mediaPlayer = null
                startTimeMillis = 0L
                pauseTimeMillis = 0L
            }
        }
    }

    @Synchronized
    fun seekTo(musicInfo: MusicInfo, seekTimeMillis: Long) {
        start(musicInfo)
        startTimeMillis = System.currentTimeMillis() - seekTimeMillis
        mediaPlayer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.seekTo(seekTimeMillis, MediaPlayer.SEEK_PREVIOUS_SYNC)
            } else {
                it.seekTo(seekTimeMillis.toInt())
            }
        }
    }
    // --------------------------------------------------

    inner class NotifyProgressRunnable : Runnable {

        override fun run() {
        }

    }

}
