package com.zaze.tribe.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.util.MediaPlayerManager
import com.zaze.utils.JsonUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-08-31 - 10:26
 */
class PlayerService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var startTimeMillis = 0L
    private var pauseTimeMillis = 0L

    companion object {
        const val PLAY = "play"
        const val PAUSE = "pause"
        const val STOP = "stop"
        const val MUSIC = "music"
    }

    private val looperExecutor = ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
            LinkedBlockingQueue(), ThreadFactory {
        val thread = Thread(it, "MEDIA_PLAYER_SERVICE")
        if (thread.isDaemon) {
            thread.isDaemon = false
        }
        thread
    })

    private val runnable = Runnable {
        while (true) {
            try {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        MediaPlayerManager.progress.set((10000 * (1.0f * it.currentPosition / it.duration)).toInt())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            SystemClock.sleep(16L)
        }
    }

    override fun onCreate() {
        super.onCreate()
        looperExecutor.execute(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        looperExecutor.remove(runnable)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.apply {
            action?.let {
                when (it) {
                    PLAY -> start(JsonUtil.parseJson(getStringExtra(MUSIC), MusicInfo::class.java))
                    PAUSE -> pause()
                    STOP -> stop()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }


    @Synchronized
    private fun start(musicInfo: MusicInfo?) {
        musicInfo?.apply {
            mediaPlayer
                    ?: MediaPlayer.create(this@PlayerService, Uri.parse("file://$localPath")).let {
                        mediaPlayer = it
                        it.setOnErrorListener { mp, what, extra ->
                            MediaPlayerManager.doNext()
                            true
                        }
                        it.setOnCompletionListener {
                            MediaPlayerManager.doNext()
                        }
                    }
            mediaPlayer?.apply {
                if (isPlaying) {
                    val curMusic = MediaPlayerManager.curMusicData.get()
                    if (curMusic != null && localPath == curMusic.localPath) {
                        this@PlayerService.pause()
                    } else {
                        this@PlayerService.stop()
                        this@PlayerService.start(musicInfo)
                    }
                } else {
                    MediaPlayerManager.curMusicData.set(musicInfo)
                    ZLog.i(ZTag.TAG_DEBUG, "start : $musicInfo")
                    start()
                    MediaPlayerManager.isPaused.set(false)
                    startTimeMillis = System.currentTimeMillis()
                }
            }
        }
    }

    @Synchronized
    private fun pause() {
        mediaPlayer?.let {
            ZLog.i(ZTag.TAG_DEBUG, "pause : ${MediaPlayerManager.curMusicData.get()}")
            if (it.isPlaying) {
                it.pause()
                MediaPlayerManager.isPaused.set(true)
                pauseTimeMillis = System.currentTimeMillis()
            }
        }
    }

    @Synchronized
    private fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                ZLog.i(ZTag.TAG_DEBUG, "stopPlaying : ${MediaPlayerManager.curMusicData.get()}")
                val player = mediaPlayer
                mediaPlayer = null
                player?.stop()
                player?.release()
                MediaPlayerManager.isPaused.set(true)
                startTimeMillis = 0L
                pauseTimeMillis = 0L
                MediaPlayerManager.progress.set(0)
            }
        }
    }

    @Synchronized
    private fun seekTo(musicInfo: MusicInfo, seekTimeMillis: Long) {
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
}