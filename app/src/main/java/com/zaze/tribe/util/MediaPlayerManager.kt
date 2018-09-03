package com.zaze.tribe.util

import android.content.Intent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.support.v4.content.ContextCompat
import com.zaze.tribe.App
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.service.PlayerService
import com.zaze.utils.JsonUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.util.*

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-07-23 - 18:58
 */
object MediaPlayerManager {
    @JvmStatic
    val curMusicData = ObservableField<MusicInfo>()

    @JvmStatic
    val progress = ObservableInt(0)
    /**
     * 仅用于UI的显示，不负责逻辑判断
     */
    @JvmStatic
    val isPaused = ObservableBoolean(true)
    /**
     * 循环模式 LoopMode
     */
    @JvmStatic
    val loopMode = ObservableInt(LoopMode.LIST)

    /**
     * 播放列表
     */
    @JvmStatic
    val playerList = ArrayList<MusicInfo>()

    @JvmStatic
    fun start(musicInfo: MusicInfo?) {
        addToPlayerlist(musicInfo)
        startService(PlayerService.PLAY, musicInfo)
    }

    @JvmStatic
    fun pause() {
        startService(PlayerService.PAUSE, null)
    }

    @JvmStatic
    fun stop() {
        startService(PlayerService.STOP, null)
    }

    @JvmStatic
    fun seekTo(musicInfo: MusicInfo, seekTimeMillis: Long) {
    }

    private fun startService(action: String, musicInfo: MusicInfo?) {
        val intent = Intent(App.INSTANCE, PlayerService::class.java)
        intent.action = action
        musicInfo?.let {
            intent.putExtra(PlayerService.MUSIC, JsonUtil.objToJson(musicInfo))
        }
        ContextCompat.startForegroundService(App.INSTANCE, intent)
    }

    @JvmStatic
    fun stopService(action: String) {
        val intent = Intent(App.INSTANCE, PlayerService::class.java)
        App.INSTANCE.stopService(intent)
    }


    @JvmStatic
    fun doNext() {
        ZLog.i(ZTag.TAG_DEBUG, "doNext")
        when (loopMode.get()) {
            LoopMode.LIST_LOOP -> {
                start(getNext(true))
            }
            LoopMode.LIST -> {
                start(getNext(false))
            }
            LoopMode.SINGLE_LOOP -> {
            }
            else -> {
            }
        }
    }

    @JvmStatic
    private fun getNext(looper: Boolean): MusicInfo? {
        val curMusic = curMusicData.get()
        if (!playerList.isEmpty()) {
            if (curMusic != null) {
                var limit = 0
                for (music in playerList) {
                    if (music.localPath == curMusic.localPath) {
                        break
                    }
                    limit++
                }
                // 定位到下一首
                limit++
                if (limit >= playerList.size) {
                    limit = 0
                }
                return playerList[limit]
            }
        }
        return if (looper) {
            curMusic
        } else {
            null
        }
    }

    @JvmStatic
    fun addToPlayerlist(musicInfo: MusicInfo?) {
        musicInfo?.let {
            addToPlayerlist(Arrays.asList(musicInfo))
        }
    }

    @JvmStatic
    fun addToPlayerlist(musicList: List<MusicInfo>) {
        musicList.let {
            val set = HashSet<String>()
            playerList.forEach {
                set.add(it.localPath)
            }
            musicList.forEach {
                if (!set.contains(it.localPath)) {
                    playerList.add(it)
                }
            }
        }
    }

    // --------------------------------------------------
    // --------------------------------------------------
    object LoopMode {
        const val LIST_LOOP = 0
        const val SINGLE_LOOP = 1
        const val SINGLE = 2
        const val LIST = 3
    }
}
