package com.zaze.tribe.util

import com.zaze.utils.FileUtil
import java.io.File

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

}
