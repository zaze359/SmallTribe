package com.zaze.tribe.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import com.zaze.tribe.music.AlbumCoverFragment
import java.util.ArrayList

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-10-02 - 0:03
 */
abstract class CustomFragmentPagerAdapter<V>(fm: FragmentManager, list: Collection<V>) : FragmentPagerAdapter(fm) {
    val dataList = ArrayList<V>()
    fun setDataList(list: Collection<V>?) {
        dataList.clear()
        list?.let {
            dataList.addAll(it)
        }
    }

    init {
        setDataList(list)
    }

    override fun getCount(): Int {
        return dataList.size
    }
}