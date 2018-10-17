package com.zaze.tribe.base

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-10-02 - 0:03
 */
abstract class CustomFragmentPagerAdapter<V>(fm: FragmentManager, list: Collection<V>) : FragmentPagerAdapter(fm) {
    val dataList = ArrayList<V>()

    init {
        setDataList(list, false)
    }

    fun setDataList(list: Collection<V>?, notify: Boolean = true) {
        dataList.clear()
        list?.let {
            dataList.addAll(it)
        }
        if (notify) {
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int {
        return dataList.size
    }
}