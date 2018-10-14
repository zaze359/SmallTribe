package com.zaze.tribe.data.source

import com.zaze.tribe.data.dto.MusicInfo
import io.reactivex.Flowable

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:28
 */
interface MusicDataSource {

    /**
     * 保存歌曲信息
     * [musicInfo] musicInfo
     * @return Int
     */
    fun saveMusicInfo(musicInfo: MusicInfo?)

    /**
     * 保存歌曲列表
     */
    fun saveMusicInfos(musicInfos: List<MusicInfo>?)

    /**
     * 获取歌曲信息
     * [musicId] musicId
     * @return  MusicInfo
     */
    fun getMusicInfo(musicId: Int): Flowable<MusicInfo>

    /**
     * 获取歌曲信息
     * [musicId] localPath localPath
     * @return  MusicInfo
     */
    fun getMusicInfo(localPath: String?): Flowable<MusicInfo>

    /**
     * 获取歌曲列表
     * @return List<MusicInfo>
     */
    fun getMusicInfoList(): Flowable<List<MusicInfo>>
}