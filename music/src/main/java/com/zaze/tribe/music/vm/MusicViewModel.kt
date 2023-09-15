package com.zaze.tribe.music.vm

import android.app.Application
import android.media.MediaMetadataRetriever
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
import com.zaze.tribe.common.base.AbsAndroidViewModel
import com.zaze.tribe.common.util.Utils
import com.zaze.tribe.common.util.set
import com.zaze.tribe.music.MusicPlayerRemote
import com.zaze.tribe.music.data.dto.Music
import com.zaze.tribe.music.data.loaders.MusicLoader
import com.zaze.utils.FileUtil
import com.zaze.utils.ToastUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-06 - 00:38
 */
open class MusicViewModel(
        private val context: Application
) : AbsAndroidViewModel(context) {
    val musicList: ObservableList<Music> = ObservableArrayList()

    val isFavorite = MutableLiveData<Boolean>()

    // ------------------------------------------------------

    fun loadMusics() {
        dataLoading.set(true)
        compositeDisposable.add(
                Observable.create<List<Music>> {
                    it.onNext(MusicLoader.getLocalMusics(context))
                    it.onComplete()
                }.subscribeOn(Schedulers.io())
                        .map { musics ->
                            musicList.apply {
                                clear()
                                addAll(musics)
                            }
                        }.doFinally {
                            dataLoading.set(false)
                        }.subscribe()
        )
    }

    fun addAndPlay(music: Music) {
        compositeDisposable.add(
                Observable.create<Music> { emitter ->
                    emitter.onNext(music)
                    emitter.onComplete()
                }.subscribeOn(Schedulers.io())
                        .map { it ->
                            MusicPlayerRemote.addToPlayingQueue(it)
                        }.observeOn(AndroidSchedulers.mainThread())
                        .map { position ->
                            MusicPlayerRemote.playAt(position)
                        }.subscribe()
        )
    }
    // ------------------------------------------------------

    private fun searchMusicFromSd(): ArrayList<Music> {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        val fileList = Utils.searchFileBySuffix(File(FileUtil.getSDCardRoot()), "mp3", true)
        val arrayList: ArrayList<Music> = ArrayList()
        for (file in fileList) {
            try {
                mediaMetadataRetriever.setDataSource(file.absolutePath)
                mediaMetadataRetriever.let {
                    val localPath = file.absolutePath
                    val name = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                            ?: file.name
                    val album = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: ""
                    val year = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)?.toInt()
                            ?: 0

                    ZLog.i(ZTag.TAG_DEBUG, "-----------------------------------------------------------------")
                    ZLog.i(ZTag.TAG_DEBUG, "localPath : $localPath")
                    ZLog.i(ZTag.TAG_DEBUG, "标题 : $name")
                    ZLog.i(ZTag.TAG_DEBUG, "专辑标题 : $album")
//                    ZLog.i(ZTag.TAG_DEBUG, "数据源艺术家 : $artist")
                    ZLog.i(ZTag.TAG_DEBUG, "创建或修改数据源的一年 : $year")
                    arrayList.add(Music(-1, name, 0, year, 0L, file.absolutePath, file.lastModified(), 0, album, 0, ""))

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return arrayList
    }

    /**
     * 显示更多
     * [music] music
     */
    fun showMore(music: Music) {
        ToastUtil.toast(context, music.data)
    }

    fun toggleFavorite(music: Music?) {

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}