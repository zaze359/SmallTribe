package com.zaze.tribe.music.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import android.support.v4.media.session.MediaSessionCompat
import androidx.databinding.ObservableArrayList
import com.zaze.tribe.music.data.dto.Music
import com.zaze.tribe.music.data.loaders.MusicLoader
import com.zaze.tribe.music.data.source.repository.MusicRepository
import com.zaze.tribe.music.service.notification.MusicNotification
import com.zaze.tribe.music.util.MusicHelper
import com.zaze.tribe.music.util.MusicPreference
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.lang.ref.WeakReference
import java.util.*

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-08-31 - 10:26
 */
class MusicService : Service(), IPlayer, MyMediaPlayer.MediaCallback {

    // ------------------------------------------------------
    private lateinit var mediaPlayer: MyMediaPlayer

    private lateinit var playerHandlerThread: HandlerThread
    private lateinit var playerHandler: PlayerHandler
    private val musicNotification = MusicNotification()

    // ------------------------------------------------------
    private var callback: PlayerCallback? = null
    private val serviceBinder = ServiceBinder()
    /**
     *
     */
    private var mediaSession: MediaSessionCompat? = null
    /**
     * 播放队列
     */
    private val playingQueue = ObservableArrayList<Music>()
    private var position = -1
    /**
     * 恢复状态
     */
    private var isRestored = false

    /**
     * 循环模式 LoopMode
     */
    private var LOOP_MODE = LoopMode.LOOP_LIST

    companion object {
        const val TAG = "MusicService"
        const val MUSIC = "music"
        const val ACTION_PLAY = "action_play"
        const val ACTION_NEXT = "action_next"
        const val ACTION_PAUSE = "action_pause"
        const val ACTION_QUIT = "action_quit"

        const val PLAY = 0
        const val PLAY_NEXT = 1
        const val RESUME = 2
        const val PAUSE = 3
        const val STOP = 4
        const val RESTORE = 5
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MyMediaPlayer(this).apply {
            mediaCallback = this@MusicService
        }
        playerHandlerThread = HandlerThread("playerHandlerThread")
        playerHandlerThread.start()
        playerHandler = PlayerHandler(this, playerHandlerThread.looper)
        playerHandler.obtainMessage(RESTORE).sendToTarget()
        musicNotification.initialize(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            playerHandlerThread.quitSafely()
        } else {
            playerHandlerThread.quit()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        setupMediaSession()
        return serviceBinder
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
        return true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let {
            restoreQueuesAndPositionIfNecessary()
            when (it) {
                ACTION_PLAY -> {
                    playAt(position)
                }
                ACTION_PAUSE -> pause()
                ACTION_NEXT -> playNext()
                ACTION_QUIT -> {
                    pause()
                    stopForeground(true)
                }
                else -> {
                }
            }
        }
        return START_STICKY
    }

    /**
     * 回复播放列表和当前播放的位置
     */
    private fun restoreQueuesAndPositionIfNecessary() {
        if (!isRestored && playingQueue.isEmpty()) {
            MusicRepository.getInstance().getPlayingQueue().apply {
                playingQueue.addAll(this)
            }
            if (playingQueue.isEmpty()) {
                playingQueue.addAll(MusicLoader.getLocalMusics(this))
            }
            val restoredPosition = MusicPreference.getLastMusicPosition()
            if (restoredPosition > 0 && !playingQueue.isEmpty() && restoredPosition < playingQueue.size) {
                val restoredTrack = MusicPreference.getLastMusicTrack()
            }
        }
        isRestored = true
    }

    @Synchronized
    override fun playAt(position: Int) {
        if (position >= 0 && position < playingQueue.size) {
            this.position = position
            mediaPlayer.apply {
                val path = if (getCurMusic().id > 0) {
                    MusicHelper.getMusicFileUri(getCurMusic().id).toString()
                } else {
                    getCurMusic().data
                }
                if (setDataSource(path)) {
                    start()
                    updateNotification(true)
                    callback?.onStart(getCurMusic())
                } else {
                    stop()
                    callback?.onError(null, -1, -1)
                }
            }
        }
    }

    @Synchronized
    override fun playNext() {
        stop()
        playAt(getNextPosition(true))
    }

    private fun getNextPosition(fromUser: Boolean): Int {
        var nextPosition = position
        if (nextPosition >= playingQueue.size - 1) {
            nextPosition = 0
        }
        when (LOOP_MODE) {
            LoopMode.LOOP_LIST -> {
                nextPosition += 1
            }
            LoopMode.LOOP_RANDOM -> {
                nextPosition = Random().nextInt(playingQueue.size)
            }
            LoopMode.LOOP_SINGLE -> {
                if (!fromUser) {
                    nextPosition += 1
                }
            }
        }
        return nextPosition
    }

    override fun enqueue(music: Music): Int {
        val position = playingQueue.indexOf(music)
        return if (position < 0) {
            playingQueue.add(music)
            MusicRepository.getInstance().saveToPlayingQueue(music)
            playingQueue.size - 1
        } else {
            position
        }
    }

    @Synchronized
    override fun resume() {
        mediaPlayer.let {
            if (!it.isPlaying()) {
                it.start()
                updateNotification(true)
                callback?.onStart(getCurMusic())
            }
        }
    }

    @Synchronized
    override fun pause() {
        mediaPlayer.let {
            ZLog.i(ZTag.TAG_DEBUG, "pause : ${getCurMusic()}")
            if (it.isPlaying()) {
                it.pause()
            }
            updateNotification(false)
            callback?.onPause()
        }
    }

    @Synchronized
    override fun stop() {
        ZLog.i(ZTag.TAG_DEBUG, "stopPlaying : ${getCurMusic()}")
        mediaPlayer.let {
            if (it.isPlaying()) {
                it.stop()
            }
            callback?.onStop()
        }
    }

    @Synchronized
    override fun seekTo(seekMillis: Int) {
        mediaPlayer.seekTo(seekMillis)
    }

    override fun onCompletion() {
        getNextPosition(false).let {
            if (it >= 0) {
                playAt(it)
            } else {
                callback?.onCompletion()
            }
        }
    }

    // ------------------------------------------------------


    override fun getPosition(): Int {
        return position
    }

    fun getCurMusic(): Music {
        return if (position >= 0 && position < playingQueue.size) {
            playingQueue[position]
        } else {
            Music.EMPTY
        }

    }

    override fun getCurProgress(): Int {
        return mediaPlayer.position()
    }

    override fun getDuration(): Int {
        return mediaPlayer.duration()
    }

    override fun getPlayingQueue(): ObservableArrayList<Music> {
        return playingQueue
    }
    // --------------------------------------------------

    @Deprecated("")
    private fun updateNotification(isPlaying: Boolean) {
        musicNotification.updateNotification(isPlaying)
//        getCurMusic().let { it ->
//            val channelId = "zaze"
//            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val notificationChannel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_LOW)
//                notificationManager.createNotificationChannel(notificationChannel)
//            }
//            val targetIntent = PendingIntent.getActivity(this, 0,
//                    Intent(this, RouterActivity::class.java).apply {
//                        putExtra(RouterActivity.ROUTER, RouterPath.main)
//                    }, PendingIntent.FLAG_UPDATE_CURRENT)
//            val remoteViews = RemoteViews(BaseApplication.INSTANCE.packageName, R.layout.music_notification_layout)
//            remoteViews.setImageViewBitmap(R.id.musicNotificationIcon, IconCache.getSmallMediaIcon(it.data))
//            remoteViews.setTextViewText(R.id.musicNotificationName, it.title)
//            remoteViews.setTextViewText(R.id.musicNotificationArtist, it.artistName)
//            remoteViews.setImageViewResource(R.id.musicNotificationPlayBtn,
//                    if (isPlaying) R.drawable.music_pause_circle_outline_black_24dp else R.drawable.music_play_circle_outline_black_24dp)
//            remoteViews.setOnClickPendingIntent(R.id.musicNotificationPlayBtn, PendingIntent.getService(this, 0,
//                    Intent(this, MusicService::class.java).apply {
//                        action = if (isPlaying) {
//                            ACTION_PAUSE
//                        } else {
//                            ACTION_PLAY
//                        }
//                    }, PendingIntent.FLAG_UPDATE_CURRENT))
//            remoteViews.setOnClickPendingIntent(R.id.musicNotificationNextBtn, PendingIntent.getService(this, 0,
//                    Intent(this, MusicService::class.java).apply {
//                        action = ACTION_NEXT
//                    }, PendingIntent.FLAG_UPDATE_CURRENT))
//            remoteViews.setOnClickPendingIntent(R.id.musicNotificationCloseBtn, PendingIntent.getService(this, 0,
//                    Intent(this, MusicService::class.java).apply {
//                        action = ACTION_QUIT
//                    }, PendingIntent.FLAG_UPDATE_CURRENT))
//
//            val builder = NotificationCompat.Builder(this, channelId).apply {
//                setCustomContentView(remoteViews)
//                setContentIntent(targetIntent)
//                //设置小图标
//                setSmallIcon(R.mipmap.music_note_white_24dp)
//            }
//            startForeground(1, builder.build())
//        }
    }


    // ------------------------------------------------------
    inner class PlayerHandler(musicService: MusicService, looper: Looper) : Handler(looper) {
        private val serviceRef = WeakReference<MusicService>(musicService)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            serviceRef.get()?.let {
                when (msg.what) {
                    PLAY -> it.playAt(msg.arg1)
                    PLAY_NEXT -> it.playNext()
                    RESUME -> it.resume()
                    PAUSE -> it.pause()
                    STOP -> it.stop()
                    RESTORE -> it.restoreQueuesAndPositionIfNecessary()
                }
            }
        }
    }
    // ------------------------------------------------------

    inner class ServiceBinder : Binder(), IPlayer {

        fun setPlayerCallback(callback: PlayerCallback) {
            this@MusicService.callback = callback
        }

        override fun playAt(position: Int) {
            playerHandler.removeMessages(PLAY)
            playerHandler.obtainMessage(PLAY, position, 0).sendToTarget()
        }

        override fun playNext() {
            playerHandler.removeMessages(PLAY_NEXT)
            playerHandler.obtainMessage(PLAY_NEXT).sendToTarget()
        }

        override fun resume() {
            playerHandler.removeMessages(RESUME)
            playerHandler.obtainMessage(RESUME).sendToTarget()
        }

        override fun pause() {
            playerHandler.removeMessages(PAUSE)
            playerHandler.obtainMessage(PAUSE).sendToTarget()
        }

        override fun stop() {
            playerHandler.removeMessages(STOP)
            playerHandler.obtainMessage(STOP).sendToTarget()
        }

        override fun seekTo(seekMillis: Int) {
            this@MusicService.seekTo(seekMillis)
        }

        override fun enqueue(music: Music): Int {
            return this@MusicService.enqueue(music)
        }

        override fun getPosition(): Int {
            return position
        }

        override fun getCurProgress(): Int {
            return this@MusicService.getCurProgress()
        }

        override fun getDuration(): Int {
            return this@MusicService.getDuration()
        }

        override fun getPlayingQueue(): ObservableArrayList<Music> {
            return playingQueue
        }
    }

    interface PlayerCallback {

        /**
         * 开始播放
         */
        fun onStart(music: Music)

        /**
         * 暂停
         */
        fun onPause()

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
        fun onError(mp: MediaPlayer?, what: Int, extra: Int)
    }

    /**
     * 循环方式
     */
    object LoopMode {
        const val LOOP_LIST = 0
        const val LOOP_SINGLE = 1
        const val SINGLE = 2
        const val LOOP_RANDOM = 3
    }
}

interface IPlayer {

    fun playAt(position: Int)

    fun playNext()

    fun resume()
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
    fun seekTo(seekMillis: Int)

    /**
     * 加入播放队列中
     */
    fun enqueue(music: Music): Int

    fun getPosition(): Int

    fun getCurProgress(): Int

    fun getDuration(): Int

    fun getPlayingQueue(): ObservableArrayList<Music>
}

