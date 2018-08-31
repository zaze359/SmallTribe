package com.zaze.tribe.music

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableList
import android.media.MediaMetadataRetriever
import android.text.TextUtils
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.data.source.repository.MusicRepository
import com.zaze.tribe.util.MediaPlayerManager
import com.zaze.tribe.util.Utils
import com.zaze.utils.FileUtil
import com.zaze.utils.ZTipUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.io.File

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-06 - 00:38
 */
class MusicViewModel(
        private val context: Application,
        private val musicRepository: MusicRepository
) : AndroidViewModel(context) {
    val dataLoading = ObservableBoolean(false)
    val musicList: ObservableList<MusicInfo> = ObservableArrayList()

    fun loadMusics() {
        dataLoading.set(true)
        var subscription: Subscription? = null
        musicRepository.getMusicInfoList()
                .map {
                    if (it.isEmpty()) {
                        //  TODO 先简单的扫描一遍
                        searchMusicFromSd()
                    } else {
                        it
                    }
                }.map {
                    musicList.apply {
                        clear()
                        addAll(it)
                    }
                }.doFinally {
                    dataLoading.set(false)
                }.subscribe(object : Subscriber<ObservableList<MusicInfo>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(s: Subscription?) {
                        subscription = s
                        subscription?.request(1)
                    }

                    override fun onNext(t: ObservableList<MusicInfo>?) {
                        dataLoading.set(false)
                    }

                    override fun onError(t: Throwable?) {
                        t?.printStackTrace()
                        dataLoading.set(false)
                    }
                })
    }


    private fun searchMusicFromSd(): ArrayList<MusicInfo> {
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
        musicRepository.saveMusicInfos(arrayList)
        return arrayList
    }

    /**
     * 开始播放
     * [music] music
     */
    fun startPlaying(music: MusicInfo) {
        MediaPlayerManager.start(music)
    }

    /**
     * 显示更多
     * [music] music
     */
    fun showMore(music: MusicInfo) {
        ZTipUtil.toast(context, music.localPath)
    }
}