package com.zaze.tribe.music.vm

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import com.zaze.tribe.music.MusicPlayerRemote
import com.zaze.tribe.service.MusicService
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-06 - 00:38
 */
class MainViewModel(
        private val context: Application
) : AndroidViewModel(context) {

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            ZLog.e(ZTag.TAG_DEBUG, "onServiceDisconnected : $name")
            MusicPlayerRemote.mBinder = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            ZLog.i(ZTag.TAG_DEBUG, "onServiceConnected : $name")
            MusicPlayerRemote.mBinder = service as MusicService.ServiceBinder
        }
    }

    fun bindService() {
        context.bindService(Intent(context, MusicService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
        restorePlayerQueue()
    }

    fun unbindService() {
        context.unbindService(serviceConnection)
    }

    // ------------------------------------------------------
    fun restorePlayerQueue() {

    }

}