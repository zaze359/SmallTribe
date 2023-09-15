package com.zaze.tribe.music.handler

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.zaze.tribe.common.util.get
import com.zaze.tribe.music.MusicPlayerRemote

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-08 - 23:34
 */
class MusicProgressHandler(private val progressCallback: Callback) :
    Handler(Looper.getMainLooper()) {

    companion object {
        private const val CMD_UPDATE_PROGRESS = 1

        /**
         * 最小间隔
         */
        private const val MIN_INTERVAL = 16L

        /**
         * 更新间隔
         */
        private const val UPDATE_INTERVAL = 800L
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        if (msg.what == CMD_UPDATE_PROGRESS) {
            val progress = MusicPlayerRemote.getProgress()
            val duration = MusicPlayerRemote.getDuration()
            progressCallback.onProgress(progress, duration)
            val delay = if (MusicPlayerRemote.isPlaying.get() == true) {
                Math.max(MIN_INTERVAL, UPDATE_INTERVAL - progress % UPDATE_INTERVAL)
            } else {
                UPDATE_INTERVAL / 2
            }
            nextMessage(delay)
        }
    }

    private fun nextMessage(delay: Long) {
//        ZLog.i(ZTag.TAG_DEBUG, "nextMessage : $delay")
        removeMessages(CMD_UPDATE_PROGRESS)
        sendEmptyMessageDelayed(CMD_UPDATE_PROGRESS, delay)
    }

    fun start() {
        nextMessage(MIN_INTERVAL)
    }

    fun stop() {
        removeMessages(CMD_UPDATE_PROGRESS)
    }

    interface Callback {
        fun onProgress(progress: Int, total: Int)
    }
}