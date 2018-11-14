package com.zaze.tribe.data.source

import com.zaze.tribe.data.dto.Music
import io.reactivex.Flowable

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:28
 */
interface MusicDataSource {

    /**
     * 保存歌曲信息
     * [music] music
     * @return Int
     */
    fun saveToPlayingQueue(music: Music?)

    /**
     * 获取歌曲信息
     * [musicId] musicId
     * @return  Music
     */
    fun getMusicInfo(musicId: Int): Flowable<Music>

    /**
     * 获取歌曲信息
     * [musicId] localPath localPath
     * @return  Music
     */
    fun getMusicInfo(localPath: String?): Flowable<Music>

    /**
     * 保存歌曲列表
     */
    fun savePlayingQueue(music: List<Music>?)

    /**
     * 获取歌曲列表
     * @return List<Music>
     */
    fun getPlayingQueue(): List<Music>
}