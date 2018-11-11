package com.zaze.tribe.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.drm.DrmStore.Playback.STOP
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.support.v4.media.session.MediaSessionCompat
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.zaze.tribe.App
import com.zaze.tribe.MainActivity
import com.zaze.tribe.R
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.util.IconCache
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-08-31 - 10:26
 */
class MusicService : Service(), IPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private var startTimeMillis = 0L
    private var pauseTimeMillis = 0L

    private val mBinder = ServiceBinder()
    private var callback: PlayerCallback? = null

    private var curMusic: MusicInfo? = null
    private var mediaSession: MediaSessionCompat? = null

    companion object {
        const val TAG = "MusicService"
        const val PLAY = "play"
        const val NEXT = "next"
        const val PAUSE = "pause"
        const val STOP = "stop"
        const val CLOSE = "close"
        const val MUSIC = "music"
    }



    override fun onBind(intent: Intent?): IBinder? {
        setupMediaSession()
        return mBinder
    }

    private fun setupMediaSession() {
        // TODO 支持媒体按键
        mediaSession = MediaSessionCompat(this, TAG).apply {
            isActive = true
        }
    }


    override fun onUnbind(intent: Intent?): Boolean {
        mediaSession?.isActive = false
        stop()
        stopForeground(true)
        return super.onUnbind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let {
            when (it) {
                PLAY -> {
                    curMusic?.apply {
                        play(this)
                    }
                }
                PAUSE -> pause()
                STOP -> stop()
                NEXT -> callback?.toNext()
                CLOSE -> {
                    stop()
                    stopForeground(true)
                }
                else -> {
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @Synchronized
    override fun play(musicInfo: MusicInfo) {
        musicInfo.apply {
            if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                return
            }
            mediaPlayer
                    ?: MediaPlayer.create(this@MusicService, Uri.parse("file://$data"))?.let { it ->
                        mediaPlayer = it
                        it.setOnErrorListener { mp, what, extra ->
                            it.release()
                            startTimeMillis = 0L
                            pauseTimeMillis = 0L
                            mediaPlayer = null
                            callback?.onError(mp, what, extra)
                            true
                        }
                        it.setOnCompletionListener {
                            it.release()
                            startTimeMillis = 0L
                            pauseTimeMillis = 0L
                            mediaPlayer = null
                            callback?.onCompletion()
                        }
                    } ?: run {
                        callback?.onError(null, -1, -1)
                        null
                    }
            mediaPlayer?.let { it ->
                curMusic = musicInfo
                callback?.preStart(musicInfo, it.duration)
                it.start()
                updateNotification(true)
                callback?.onStart(musicInfo)
                startTimeMillis = System.currentTimeMillis()
            }
        }
    }

    @Synchronized
    override fun pause() {
        mediaPlayer?.let {
            ZLog.i(ZTag.TAG_DEBUG, "pause : $curMusic")
            if (it.isPlaying) {
                it.pause()
                updateNotification(false)
                callback?.onPause()
                pauseTimeMillis = System.currentTimeMillis()
            }
        }
    }

    @Synchronized
    override fun stop() {
        ZLog.i(ZTag.TAG_DEBUG, "stopPlaying : $curMusic")
        mediaPlayer?.let {
            val player = it
            startTimeMillis = 0L
            pauseTimeMillis = 0L
            mediaPlayer = null
            if (player.isPlaying) {
                player.stop()
                player.release()
                callback?.onStop()
            }

        }
    }

    @Synchronized
    override fun seekTo(seekTimeMillis: Long) {
        startTimeMillis = System.currentTimeMillis() - seekTimeMillis
        mediaPlayer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.seekTo(seekTimeMillis, MediaPlayer.SEEK_PREVIOUS_SYNC)
            } else {
                it.seekTo(seekTimeMillis.toInt())
            }
        }
    }

    override fun getCurProgress(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    override fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }
    // --------------------------------------------------

    private fun updateNotification(isPlaying: Boolean) {
        curMusic?.let { it ->
            val channelId = "zaze"
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_LOW)
                notificationManager.createNotificationChannel(notificationChannel)
            }
            val targetIntent = PendingIntent.getActivity(this, 0,
                    Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
            val remoteViews = RemoteViews(App.INSTANCE.packageName, R.layout.music_notification_layout)
            remoteViews.setImageViewBitmap(R.id.musicNotificationIcon, IconCache.getSmallMediaIcon(it.data))
            remoteViews.setTextViewText(R.id.musicNotificationName, it.title)
            remoteViews.setTextViewText(R.id.musicNotificationArtist, it.artistName)
            remoteViews.setImageViewResource(R.id.musicNotificationPlayBtn,
                    if (isPlaying) R.drawable.ic_pause_circle_outline_black_24dp else R.drawable.ic_play_circle_outline_black_24dp)
            remoteViews.setOnClickPendingIntent(R.id.musicNotificationPlayBtn, PendingIntent.getService(this, 0,
                    Intent(this, MusicService::class.java).apply {
                        action = if (isPlaying) {
                            PAUSE
                        } else {
                            PLAY
                        }
                    }, PendingIntent.FLAG_UPDATE_CURRENT))

            remoteViews.setOnClickPendingIntent(R.id.musicNotificationNextBtn, PendingIntent.getService(this, 0,
                    Intent(this, MusicService::class.java).apply {
                        action = NEXT
                    }, PendingIntent.FLAG_UPDATE_CURRENT))
            remoteViews.setOnClickPendingIntent(R.id.musicNotificationCloseBtn, PendingIntent.getService(this, 0,
                    Intent(this, MusicService::class.java).apply {
                        action = CLOSE
                    }, PendingIntent.FLAG_UPDATE_CURRENT))

            val builder = NotificationCompat.Builder(this, channelId).apply {
                setCustomContentView(remoteViews)
                setContentIntent(targetIntent)
                //设置小图标
                setSmallIcon(R.mipmap.ic_music_note_white_24dp)
            }
            startForeground(1, builder.build())
        }
    }

    inner class ServiceBinder : Binder(), IPlayer {
        fun setPlayerCallback(callback: PlayerCallback) {
            this@MusicService.callback = callback
        }

        override fun play(musicInfo: MusicInfo) {
            this@MusicService.play(musicInfo)
        }

        override fun pause() {
            this@MusicService.pause()
        }

        override fun stop() {
            this@MusicService.stop()
        }

        override fun seekTo(seekTimeMillis: Long) {
            this@MusicService.seekTo(seekTimeMillis)
        }

        override fun getCurProgress(): Int {
            return this@MusicService.getCurProgress()
        }

        override fun getDuration(): Int {
            return this@MusicService.getDuration()
        }
    }

    inner class PlayerHandler(looper: Looper) : Handler(looper)

    interface PlayerCallback {

        /**
         * 准备开始播放
         */
        fun preStart(musicInfo: MusicInfo, duration: Int)

        /**
         * 开始播放
         */
        fun onStart(musicInfo: MusicInfo)

        /**
         * 暂停
         */
        fun onPause()

        /**
         * 停止
         */
        fun onStop()

        /**
         * 下一首
         */
        fun toNext()

        /**
         * 播放完成
         */
        fun onCompletion()

        /**
         * 出错
         */
        fun onError(mp: MediaPlayer?, what: Int, extra: Int)
    }
}

interface IPlayer {

    /**
     * 开始播放
     */
    fun play(musicInfo: MusicInfo)

    /**
     * 暂停
     */
    fun pause()

    /**
     * 停止
     */
    fun stop()

    /**
     * 拖动
     */
    fun seekTo(seekTimeMillis: Long)

    fun getCurProgress(): Int

    fun getDuration(): Int
}


