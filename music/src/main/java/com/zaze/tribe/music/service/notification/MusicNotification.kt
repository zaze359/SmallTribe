package com.zaze.tribe.music.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import com.zaze.tribe.music.service.MusicService

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-17 - 17:01
 */
class MusicNotification {
    private lateinit var notificationManager: NotificationManager
    private lateinit var service: MusicService

    fun initialize(service: MusicService) {
        this.service = service
        notificationManager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "zaze"
            val notificationChannel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    /**
     * 停止
     */
    fun stop() {

    }

    private fun updateNotification() {
        service.getCurMusic().let { music ->

        }
//        val targetIntent = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
//        val remoteViews = RemoteViews(App.INSTANCE.packageName, R.layout.music_notification_layout)
//        remoteViews.setImageViewBitmap(R.id.musicNotificationIcon, IconCache.getSmallMediaIcon(it.data))
//        remoteViews.setTextViewText(R.id.musicNotificationName, it.title)
//        remoteViews.setTextViewText(R.id.musicNotificationArtist, it.artistName)
//        remoteViews.setImageViewResource(R.id.musicNotificationPlayBtn,
//                if (isPlaying) R.drawable.ic_pause_circle_outline_black_24dp else R.drawable.ic_play_circle_outline_black_24dp)
//        remoteViews.setOnClickPendingIntent(R.id.musicNotificationPlayBtn, PendingIntent.getService(this, 0,
//                Intent(this, MusicService::class.java).apply {
//                    action = if (isPlaying) {
//                        MusicService.ACTION_PAUSE
//                    } else {
//                        MusicService.ACTION_PLAY
//                    }
//                }, PendingIntent.FLAG_UPDATE_CURRENT))
//
//        remoteViews.setOnClickPendingIntent(R.id.musicNotificationNextBtn, PendingIntent.getService(this, 0,
//                Intent(this, MusicService::class.java).apply {
//                    action = MusicService.ACTION_NEXT
//                }, PendingIntent.FLAG_UPDATE_CURRENT))
//        remoteViews.setOnClickPendingIntent(R.id.musicNotificationCloseBtn, PendingIntent.getService(this, 0,
//                Intent(this, MusicService::class.java).apply {
//                    action = MusicService.ACTION_QUIT
//                }, PendingIntent.FLAG_UPDATE_CURRENT))
//
//        val builder = NotificationCompat.Builder(this, channelId).apply {
//            setCustomContentView(remoteViews)
//            setContentIntent(targetIntent)
//            //设置小图标
//            setSmallIcon(R.mipmap.ic_music_note_white_24dp)
//        }
    }

    private fun buildActionIntent(context: Context, action: String): Intent {
        return Intent(context, MusicService::class.java).also { it ->
            it.action = action
        }
    }
}