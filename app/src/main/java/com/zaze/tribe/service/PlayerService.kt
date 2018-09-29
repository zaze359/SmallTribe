package com.zaze.tribe.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import com.zaze.tribe.App
import com.zaze.tribe.MainActivity
import com.zaze.tribe.R
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.util.IconCache
import com.zaze.utils.JsonUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-08-31 - 10:26
 */
class PlayerService : Service(), IPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var startTimeMillis = 0L
    private var pauseTimeMillis = 0L

    private val mBinder = ServiceBinder()
    private var callback: PlayerCallback? = null

    private lateinit var curMusic: MusicInfo

    companion object {
        const val PLAY = "play"
        const val NEXT = "next"
        const val PAUSE = "pause"
        const val STOP = "stop"
        const val CLOSE = "close"
        const val MUSIC = "music"
    }

    private val handlerThread = HandlerThread("MEDIA_PLAYER_SERVICE").apply {
        start()
    }
    private val playerHandler = Handler(handlerThread.looper)
//    private val looperExecutor = ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
//            LinkedBlockingQueue(), ThreadFactory {
//        val thread = Thread(it, "MEDIA_PLAYER_SERVICE")
//        if (thread.isDaemon) {
//            thread.isDaemon = false
//        }
//        thread
//    })

    private val progressRunnable = object : Runnable {
        override fun run() {
            try {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        callback?.onProgress(curMusic, (10000 * (1.0f * it.currentPosition / it.duration)).toInt())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            playerHandler.postDelayed(this, 16L)
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        playerHandler.removeCallbacks(progressRunnable)
        playerHandler.post(progressRunnable)
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stop()
        playerHandler.removeCallbacks(progressRunnable)
        stopForeground(true)
        return super.onUnbind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.apply {
            action?.let {
                when (it) {
                    PLAY -> start(JsonUtil.parseJson(getStringExtra(MUSIC), MusicInfo::class.java))
                    PAUSE -> pause()
                    STOP -> stop()
                    NEXT -> callback?.onCompletion()
                    CLOSE -> {
                        stop()
                        stopForeground(true)
                    }
                    else -> {
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @Synchronized
    override fun start(musicInfo: MusicInfo) {
        if (musicInfo.id <= 0) {
            return
        }
        musicInfo.apply {
            mediaPlayer
                    ?: MediaPlayer.create(this@PlayerService, Uri.parse("file://$localPath")).let { it ->
                        mediaPlayer = it
                        it.setOnErrorListener { mp, what, extra ->
                            callback?.onError(mp, what, extra)
                            true
                        }
                        it.setOnCompletionListener {
                            callback?.onCompletion()
                        }
                    }
            mediaPlayer?.apply {
                if (isPlaying) {
                    if (localPath == curMusic.localPath) {
                        this@PlayerService.pause()
                    } else {
                        this@PlayerService.stop()
                        this@PlayerService.start(musicInfo)
                    }
                } else {
                    curMusic = musicInfo
                    callback?.preStart(musicInfo)
                    start()
                    updateNotification(true)
                    callback?.onStart(musicInfo)
                    startTimeMillis = System.currentTimeMillis()
                }
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
        mediaPlayer?.let {
            if (it.isPlaying) {
                ZLog.i(ZTag.TAG_DEBUG, "stopPlaying : $curMusic")
                val player = mediaPlayer
                mediaPlayer = null
                player?.stop()
                player?.release()
                callback?.onStop()
                startTimeMillis = 0L
                pauseTimeMillis = 0L
            }
        }
    }

    @Synchronized
    override fun seekTo(musicInfo: MusicInfo, seekTimeMillis: Long) {
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

    private fun updateNotification(isPlaying: Boolean) {
        playerHandler.post {
            val channelId = "zaze"
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_LOW)
                notificationManager.createNotificationChannel(notificationChannel)
            }
            val targetIntent = PendingIntent.getActivity(this, 0,
                    Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
            val remoteViews = RemoteViews(App.INSTANCE.packageName, R.layout.music_notification_layout)
            remoteViews.setImageViewBitmap(R.id.music_notification_icon_iv, IconCache.getSmallMediaIcon(curMusic.localPath))
            remoteViews.setTextViewText(R.id.music_notification_name_iv, curMusic.name)
            remoteViews.setTextViewText(R.id.music_notification_auth_iv, curMusic.artist)
            remoteViews.setImageViewResource(R.id.music_notification_play_iv,
                    if (isPlaying) R.drawable.ic_pause_circle_outline_black_24dp else R.drawable.ic_play_circle_outline_black_24dp)
            remoteViews.setOnClickPendingIntent(R.id.music_notification_play_iv, PendingIntent.getService(this, 0,
                    Intent(this, PlayerService::class.java).apply {
                        action = PLAY
                        putExtra(MUSIC, JsonUtil.objToJson(curMusic))
                    }, PendingIntent.FLAG_UPDATE_CURRENT))

            remoteViews.setOnClickPendingIntent(R.id.music_notification_next_iv, PendingIntent.getService(this, 0,
                    Intent(this, PlayerService::class.java).apply {
                        action = NEXT
                        putExtra(MUSIC, JsonUtil.objToJson(curMusic))
                    }, PendingIntent.FLAG_UPDATE_CURRENT))
            remoteViews.setOnClickPendingIntent(R.id.music_notification_close_iv, PendingIntent.getService(this, 0,
                    Intent(this, PlayerService::class.java).apply {
                        action = CLOSE
                        putExtra(MUSIC, JsonUtil.objToJson(curMusic))
                    }, PendingIntent.FLAG_UPDATE_CURRENT))

            val builder = NotificationCompat.Builder(this, channelId).apply {
                setCustomContentView(remoteViews)
                setContentIntent(targetIntent)
                //设置小图标
                setSmallIcon(R.mipmap.ic_music_note_white_24dp)
            }

            val notification = builder.build()
            startForeground(1, notification)
        }
    }

    inner class ServiceBinder : Binder(), IPlayer {

        fun setPlayerCallback(callback: PlayerCallback) {
            this@PlayerService.callback = callback
        }

        override fun start(musicInfo: MusicInfo) {
            this@PlayerService.start(musicInfo)
        }

        override fun pause() {
            this@PlayerService.pause()
        }

        override fun stop() {
            this@PlayerService.stop()
        }

        override fun seekTo(musicInfo: MusicInfo, seekTimeMillis: Long) {
            this@PlayerService.seekTo(musicInfo, seekTimeMillis)
        }
    }

    interface PlayerCallback {

        /**
         * 准备开始播放
         */
        fun preStart(musicInfo: MusicInfo)

        /**
         * 开始播放
         */
        fun onStart(musicInfo: MusicInfo)

        /**
         * 暂停
         */
        fun onPause()

        /**
         * 进度回掉
         */
        fun onProgress(musicInfo: MusicInfo, progress: Int)

        /**
         * 停止
         */
        fun onStop()

        /**
         * 播放完成
         */
        fun onCompletion()

        /**
         * 出错
         */
        fun onError(mp: MediaPlayer, what: Int, extra: Int)
    }
}

interface IPlayer {

    fun start(musicInfo: MusicInfo)

    fun pause()

    fun stop()

    fun seekTo(musicInfo: MusicInfo, seekTimeMillis: Long)
}
