package com.zaze.tribe.music

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.data.source.repository.MusicRepository
import com.zaze.tribe.service.PlayerService
import com.zaze.tribe.util.Utils
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File
import java.time.Duration
import java.util.*
import kotlin.collections.ArrayList

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-06 - 00:38
 */
object MusicPlayerRemote {

    /**
     * 播放列表
     */
    @JvmStatic
    val playerList = ObservableArrayList<MusicInfo>()

    /**
     * 当前播放的页面
     */
    @JvmStatic
    val curMusicData = ObservableField<MusicInfo>()

    /**
     * 仅用于UI的显示，不负责逻辑判断
     */
    @JvmStatic
    val isPlaying = ObservableBoolean(false)

    /**
     * 循环模式 LoopMode
     */
    private val LOOP_MODE = ObservableInt(LoopMode.LIST_LOOP)

    // ------------------------------------------------------
    var mBinder: PlayerService.ServiceBinder? = null
        set(value) {
            field = value
            field?.setPlayerCallback(object : PlayerService.PlayerCallback {
                override fun preStart(musicInfo: MusicInfo, duration: Int) {
                    curMusicData.set(musicInfo)
                    ZLog.i(ZTag.TAG_DEBUG, "preStart")
                }


                override fun onStart(musicInfo: MusicInfo) {
                    isPlaying.set(true)
                    ZLog.i(ZTag.TAG_DEBUG, "onStart : $musicInfo")
                }

                override fun onPause() {
                    isPlaying.set(false)
                    ZLog.i(ZTag.TAG_DEBUG, "onPause")
                }

                override fun onStop() {
                    isPlaying.set(false)
                    ZLog.i(ZTag.TAG_DEBUG, "onStop")
                }

                override fun toNext() {
                    // 下一首
                    stop()
                    getNext(true)?.let {
                        start(it)
                    }
                }

                override fun onCompletion() {
                    doNext()
                    ZLog.i(ZTag.TAG_DEBUG, "onCompletion")
                }

                override fun onError(mp: MediaPlayer?, what: Int, extra: Int) {
                    stop()
//            getNext(false)?.let {
//                start(it)
//            }
                    ZLog.i(ZTag.TAG_DEBUG, "onError")
                }
            })
        }

    // ------------------------------------------------------

    fun scanMusicFromSd(): ArrayList<MusicInfo> {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        val fileList = Utils.searchFileBySuffix(File(FileUtil.getSDCardRoot()), "mp3", true)
        val arrayList: ArrayList<MusicInfo> = ArrayList()
        for (file in fileList) {
            try {
                mediaMetadataRetriever.setDataSource(file.absolutePath)
                mediaMetadataRetriever.let {
                    //                    MusicInfo().apply {
//                        artist = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: ""
//                        if (!TextUtils.isEmpty(artist)) {
//                            localPath = file.absolutePath
//                            name = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: file.name
//                            album = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: ""
//                            year = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)?.toInt() ?: 0
//                            ZLog.i(ZTag.TAG_DEBUG, "-----------------------------------------------------------------")
//                            ZLog.i(ZTag.TAG_DEBUG, "localPath : $localPath")
//                            ZLog.i(ZTag.TAG_DEBUG, "标题 : $name")
//                            ZLog.i(ZTag.TAG_DEBUG, "专辑标题 : $album")
//                            ZLog.i(ZTag.TAG_DEBUG, "数据源艺术家 : $artist")
//                            ZLog.i(ZTag.TAG_DEBUG, "创建或修改数据源的一年 : $year")
//
//                            ZLog.i(ZTag.TAG_DEBUG, "相关联的艺术家 : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)}")
//                            ZLog.i(ZTag.TAG_DEBUG, "数据源作者的信息 : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR)}")
//                            ZLog.i(ZTag.TAG_DEBUG, "METADATA_KEY_WRITER : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_WRITER)}")
//                            ZLog.i(ZTag.TAG_DEBUG, "比特率(bit/s) : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)}")
//                            ZLog.i(ZTag.TAG_DEBUG, "数据源的作曲家的信息 : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER)}")
//                            ZLog.i(ZTag.TAG_DEBUG, "持续时间 : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)}")
//                            ZLog.i(ZTag.TAG_DEBUG, "媒体是否包含音频内容 : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO)}")
//                            ZLog.i(ZTag.TAG_DEBUG, "媒体是否包含视频内容 : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO)}")
//                            ZLog.i(ZTag.TAG_DEBUG, "数据源MIME类型 : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)}")
//
//                            ZLog.i(ZTag.TAG_DEBUG, "音乐专辑编辑状态 : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPILATION)}")
//                            ZLog.i(ZTag.TAG_DEBUG, "创建数据源时的日期 : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE)}")
//
//                            ZLog.i(ZTag.TAG_DEBUG, "METADATA_KEY_CD_TRACK_NUMBER : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)}")
//                            ZLog.i(ZTag.TAG_DEBUG, "METADATA_KEY_DISC_NUMBER : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER)}")
//                            ZLog.i(ZTag.TAG_DEBUG, "METADATA_KEY_GENRE : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)}")
//
//                            ZLog.i(ZTag.TAG_DEBUG, "METADATA_KEY_LOCATION : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION)}")
//                            ZLog.i(ZTag.TAG_DEBUG, "METADATA_KEY_NUM_TRACKS : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS)}")
//                            ZLog.i(ZTag.TAG_DEBUG, "METADATA_KEY_VIDEO_HEIGHT : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)}")
//                            ZLog.i(ZTag.TAG_DEBUG, "METADATA_KEY_VIDEO_ROTATION : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)}")
//                            ZLog.i(ZTag.TAG_DEBUG, "METADATA_KEY_VIDEO_WIDTH : ${it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)}")
//                            ZLog.i(ZTag.TAG_DEBUG, "----------------------------------------------------------------------------------")
//                            arrayList.add(this)
//                        }
//                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }

        }
        MusicRepository.getInstance().saveMusicInfos(arrayList)
        return arrayList
    }

    /**
     * 开始播放
     * [musicInfo] music
     */
    @JvmOverloads
    @JvmStatic
    fun start(musicInfo: MusicInfo? = curMusicData.get()) {
        musicInfo?.let {
            mBinder?.start(musicInfo)
        }
    }

    @Synchronized
    @JvmStatic
    fun startAt(position: Int) {
        if (position >= 0 && position < playerList.size) {
            start(playerList[position])
        }
    }

    @JvmStatic
    fun pause() {
        mBinder?.pause()
    }

    @JvmStatic
    fun stop() {
        mBinder?.stop()
    }

    @JvmStatic
    fun seekTo(seekTimeMillis: Long) {
        mBinder?.seekTo(seekTimeMillis)
    }

    private fun doNext() {
        ZLog.i(ZTag.TAG_DEBUG, "doNext")
        when (LOOP_MODE.get()) {
            LoopMode.LIST_LOOP -> {
                getNext(true)?.let {
                    stop()
                    start(it)
                }
            }
            LoopMode.LIST -> {
                getNext(false)?.let {
                    stop()
                    start(it)
                }
            }
            LoopMode.SINGLE_LOOP -> {
            }
            else -> {
            }
        }
    }

    @Synchronized
    private fun getNext(looper: Boolean): MusicInfo? {
        val curMusic = curMusicData.get()
        if (!playerList.isEmpty()) {
            if (curMusic != null) {
                var limit = 0
                for (music in playerList) {
                    if (music.data != curMusic.data) {
                        limit++
                    } else {
                        break
                    }
                }
                // 定位到下一首
                limit++
                if (limit >= playerList.size) {
                    return if (looper) {
                        playerList[0]
                    } else {
                        return null
                    }
                } else {
                    return playerList[limit]
                }
            }
        }
        return if (looper) {
            curMusic
        } else {
            null
        }
    }

    @Synchronized
    fun addToPlayerList(musicList: List<MusicInfo>): List<MusicInfo> {
        val set = HashSet<Int>()
        for (old in playerList) {
            set.add(old.id)
        }
        val newList = ArrayList<MusicInfo>()
        for (new in musicList) {
            if (!set.contains(new.id)) {
                newList.add(new)
            }
        }
        if (!newList.isEmpty()) {
            playerList.addAll(newList)
        }
        return newList
    }

    @Synchronized
    @JvmStatic
    fun getCurPosition(): Int {
        return playerList.indexOf(curMusicData.get())
    }

    fun getProgress() : Int {
        return mBinder?.getCurProgress() ?: 0
    }

    fun getDuration() : Int {
        return mBinder?.getDuration() ?: 0
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