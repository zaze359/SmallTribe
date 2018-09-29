package com.zaze.tribe.music

import android.databinding.*
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.text.TextUtils
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.data.source.repository.MusicRepository
import com.zaze.tribe.service.PlayerService
import com.zaze.tribe.util.Utils
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File
import java.util.*

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-06 - 00:38
 */
object MusicPlayerRemote {

    /**
     * 播放列表
     */
    val playerList = ArrayList<MusicInfo>()

    /**
     * 当前播放的页面
     */
    @JvmStatic
    val curMusicData = ObservableField<MusicInfo>(MusicInfo())

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
    val loopMode = ObservableInt(LoopMode.LIST)

    // ------------------------------------------------------
    var mBinder: PlayerService.ServiceBinder? = null
        set(value) {
            field = value
            field?.setPlayerCallback(playerCallback)
        }

    private val playerCallback = object : PlayerService.PlayerCallback {

        override fun preStart(musicInfo: MusicInfo) {
            curMusicData.set(musicInfo)
            ZLog.i(ZTag.TAG_DEBUG, "preStart")
        }

        override fun onStart(musicInfo: MusicInfo) {
            isPaused.set(false)
            ZLog.i(ZTag.TAG_DEBUG, "onStart : $musicInfo")
        }

        override fun onPause() {
            isPaused.set(true)
            ZLog.i(ZTag.TAG_DEBUG, "onPause")
        }

        override fun onProgress(musicInfo: MusicInfo, progress: Int) {
//            curMusicData.get()?: curMusicData.set(musicInfo)
            this@MusicPlayerRemote.progress.set(progress)
        }

        override fun onStop() {
            isPaused.set(true)
            progress.set(0)
            ZLog.i(ZTag.TAG_DEBUG, "onStop")
        }

        override fun toNext() {
            getNext(true)?.let {
                stop()
                start(it)
            }
        }

        override fun onCompletion() {
            doNext()
            ZLog.i(ZTag.TAG_DEBUG, "onCompletion")
        }

        override fun onError(mp: MediaPlayer, what: Int, extra: Int) {
            doNext()
            ZLog.i(ZTag.TAG_DEBUG, "onError")
        }
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
                    MusicInfo().apply {
                        artist = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: ""
                        if (!TextUtils.isEmpty(artist)) {
                            localPath = file.absolutePath
                            name = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: file.name
                            album = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: ""
                            year = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)?.toInt() ?: 0
                            ZLog.i(ZTag.TAG_DEBUG, "-----------------------------------------------------------------")
                            ZLog.i(ZTag.TAG_DEBUG, "localPath : $localPath")
                            ZLog.i(ZTag.TAG_DEBUG, "标题 : $name")
                            ZLog.i(ZTag.TAG_DEBUG, "专辑标题 : $album")
                            ZLog.i(ZTag.TAG_DEBUG, "数据源艺术家 : $artist")
                            ZLog.i(ZTag.TAG_DEBUG, "创建或修改数据源的一年 : $year")

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
                            ZLog.i(ZTag.TAG_DEBUG, "----------------------------------------------------------------------------------")
                            arrayList.add(this)
                        }
                    }
                }
            } catch (e: Exception) {
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
    @JvmStatic
    @JvmOverloads
    fun start(musicInfo: MusicInfo? = curMusicData.get()) {
        musicInfo?.let {
            mBinder?.start(musicInfo)
            addToPlayerList(it)
        }
    }

    @JvmStatic
    fun pause() {
        mBinder?.pause()
    }

    fun stop() {
        mBinder?.stop()
    }

    fun seekTo(musicInfo: MusicInfo, seekTimeMillis: Long) {
        mBinder?.seekTo(musicInfo, seekTimeMillis)
    }

    fun doNext() {
        ZLog.i(ZTag.TAG_DEBUG, "doNext")
        when (loopMode.get()) {
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

    private fun addToPlayerList(musicInfo: MusicInfo?) {
        musicInfo?.let {
            addToPlayerList(Arrays.asList(musicInfo))
        }
    }

    private fun addToPlayerList(musicList: List<MusicInfo>) {
        musicList.let {
            val set = HashSet<String>()
            for(old in playerList) {
                set.add(old.localPath)
            }
            for(new in it) {
                if (!set.contains(new.localPath)) {
                    playerList.add(new)
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