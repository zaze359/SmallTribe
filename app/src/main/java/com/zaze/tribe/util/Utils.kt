package com.zaze.tribe.util

import com.zaze.utils.FileUtil
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

}
