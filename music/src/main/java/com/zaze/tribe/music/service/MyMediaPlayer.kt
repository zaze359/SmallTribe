package com.zaze.tribe.music.service

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.PowerManager

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-15 - 3:12
 */
class MyMediaPlayer(private val context: Context) : MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private var mediaPlayer = MediaPlayer()
    private var isInitialized = false

    init {
        mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
    }

    fun setDataSource(path: String): Boolean {
        isInitialized = false
        isInitialized = mediaPlayer.run {
            try {
                reset()
                setOnPreparedListener(null)
                if (path.startsWith("content://")) {
                    setDataSource(context, Uri.parse(path))
                } else {
                    setDataSource(path)
                }
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                prepare()
                setOnCompletionListener(this@MyMediaPlayer)
                setOnErrorListener(this@MyMediaPlayer)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
        return isInitialized
    }

    fun start() {
        try {
            mediaPlayer.start()
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }

    fun pause() {
        try {
            mediaPlayer.pause()
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }

    fun seekTo(msec: Int) {
        try {
            mediaPlayer.seekTo(msec)
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        reset()
    }

    fun reset() {
        try {
            mediaPlayer.reset()
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }

    fun release() {
        stop()
        try {
            mediaPlayer.release()
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }

    fun isPlaying(): Boolean {
        return isInitialized && mediaPlayer.isPlaying
    }

    fun duration(): Int {
        if(!isInitialized) {
            return -1
        }
        return mediaPlayer.duration
    }

    fun position(): Int {
        if(!isInitialized) {
            return -1
        }
        return mediaPlayer.currentPosition
    }

    // ------------------------------------------------------
    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        return true
    }

    override fun onCompletion(mp: MediaPlayer?) {
    }


}