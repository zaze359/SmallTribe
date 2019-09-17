package com.zaze.tribe.common.util

import android.media.MediaMetadataRetriever
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File
import java.util.*

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-07-11 - 12:16
 */
object Utils {

    @JvmStatic
    private val blackDirSet = arrayListOf(
            "${FileUtil.getSDCardRoot()}/Android",
            "${FileUtil.getSDCardRoot()}/tencent/MobileQQ"
    )

    /**
     * [dir] 查询目标目录下符合fileName的文件和文件夹
     * [fileName] fileName
     * [isDeep] 是否深度查询
     */
    @JvmStatic
    @JvmOverloads
    fun searchFileAndDir(dir: File, fileName: String, isDeep: Boolean = false): ArrayList<File> {
        val searchedFileList = ArrayList<File>()
        if (dir.exists() && dir.isDirectory) {
            val childFileList = dir.listFiles()
            for (childFile in childFileList) {
                if (childFile.name == fileName) {
                    searchedFileList.add(childFile)
                }
                if (isDeep && childFile.isDirectory) {
                    searchedFileList.addAll(searchFileAndDir(childFile, fileName, isDeep))
                }
            }
        }
        return searchedFileList
    }

    /**
     * [dir] 目标目录下查询文件
     * [suffix] suffix
     * [isDeep] 是否深度查询
     */
    @JvmStatic
    @JvmOverloads
    fun searchFileBySuffix(dir: File, suffix: String, isDeep: Boolean = false): ArrayList<File> {
        val searchedFileList = ArrayList<File>()
        if (dir.exists() && dir.isDirectory) {
            val childFileList = dir.listFiles()
            for (childFile in childFileList) {
                ZLog.i(ZTag.TAG_FILE, "childFile : ${childFile.absoluteFile}")
                if (childFile.isFile && childFile.name.endsWith(".$suffix", true)) {
                    searchedFileList.add(childFile)
                }
                if (isDeep && childFile.isDirectory && !blackDirSet.contains(childFile.absolutePath)) {
                    searchedFileList.addAll(searchFileBySuffix(childFile, suffix, isDeep))
                }
            }
        }
        return searchedFileList
    }

    @JvmStatic
    fun getDurationString(duration: Long): String {
        val minutes = (duration / 1000) / 60
        val seconds = (duration / 1000) % 60
        return if (minutes < 60) {
            String.format(Locale.getDefault(), "%01d:%02d", minutes, seconds)
        } else {
            String.format(Locale.getDefault(), "%d:%02d:%02d", minutes / 60, minutes % 60, seconds)
        }
    }


    @Deprecated("")
    fun scanMusicFromSd() {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        val fileList = searchFileBySuffix(File(FileUtil.getSDCardRoot()), "mp3", true)
        for (file in fileList) {
            try {
                mediaMetadataRetriever.setDataSource(file.absolutePath)
                mediaMetadataRetriever.let {
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
//                        }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }

        }
    }


}
