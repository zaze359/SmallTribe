package com.zaze.tribe.music.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.zaze.tribe.common.BaseApplication
import com.zaze.tribe.music.R
import com.zaze.tribe.music.service.MusicService
import com.zaze.tribe.music.util.MediaIconCache

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-17 - 17:01
 */
class MusicNotification {
    private lateinit var notificationManager: NotificationManager
    private lateinit var service: MusicService
    private lateinit var remoteViews: RemoteViews
    private lateinit var builder: NotificationCompat.Builder

    companion object {
        const val ID = 1
    }

    fun initialize(service: MusicService) {
        this.service = service
        notificationManager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "zaze"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        remoteViews = RemoteViews(BaseApplication.INSTANCE.packageName, R.layout.music_notification_layout)
        builder = NotificationCompat.Builder(service, channelId).apply {
            setCustomContentView(remoteViews)
            setContentIntent(PendingIntent.getActivity(service, 0,
                    service.packageManager.getLaunchIntentForPackage(BaseApplication.INSTANCE.packageName), PendingIntent.FLAG_UPDATE_CURRENT))
            //设置小图标
            setSmallIcon(R.mipmap.music_note_white_24dp)
        }
    }

    fun updateNotification(isPlaying: Boolean) {
        service.getCurMusic().let { music ->
            remoteViews.setImageViewBitmap(R.id.musicNotificationIcon, MediaIconCache.getSmallMediaIcon(music.data))
            remoteViews.setTextViewText(R.id.musicNotificationName, music.title)
            remoteViews.setTextViewText(R.id.musicNotificationArtist, music.artistName)
            remoteViews.setImageViewResource(R.id.musicNotificationPlayBtn,
                    if (isPlaying) R.drawable.music_pause_circle_outline_black_24dp else R.drawable.music_play_circle_outline_black_24dp
            )
            remoteViews.setOnClickPendingIntent(R.id.musicNotificationPlayBtn, buildPendingIntent(
                    if (isPlaying) MusicService.ACTION_PAUSE else MusicService.ACTION_PLAY)
            )
            remoteViews.setOnClickPendingIntent(R.id.musicNotificationNextBtn, buildPendingIntent(MusicService.ACTION_NEXT))
            remoteViews.setOnClickPendingIntent(R.id.musicNotificationCloseBtn, buildPendingIntent(MusicService.ACTION_QUIT))
        }
        service.startForeground(ID, builder.build())
    }

    private fun buildPendingIntent(action: String): PendingIntent {
        return PendingIntent.getService(service, 0, buildActionIntent(action), PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun buildActionIntent(action: String): Intent {
        return Intent(service, MusicService::class.java).also {
            it.action = action
        }
    }
}