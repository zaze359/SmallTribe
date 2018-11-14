package com.zaze.tribe.service

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

    init {
        mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
    }

    fun setDataSource(path: String): Boolean {
        mediaPlayer.run {
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
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
            setOnCompletionListener(this@MyMediaPlayer)
            setOnErrorListener(this@MyMediaPlayer)
            return true
        }
    }

    fun start() {
        mediaPlayer.start()
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun seekTo(msec: Int) {
        mediaPlayer.seekTo(msec)
    }

    fun stop() {
        reset()
    }

    fun reset() {
        mediaPlayer.reset()
    }

    fun release() {
        stop()
        mediaPlayer.release()
    }

    fun isPlaying(): Boolean {
        return try {
            mediaPlayer.isPlaying
        } catch (e: Exception) {
            false
        }
    }

    fun duration(): Int {
        return try {
            mediaPlayer.duration
        } catch (e: Exception) {
            -1
        }
    }

    fun position(): Int {
        return try {
            mediaPlayer.currentPosition
        } catch (e: Exception) {
            -1
        }
    }

    // ------------------------------------------------------
    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        return true
    }

    override fun onCompletion(mp: MediaPlayer?) {
    }


}