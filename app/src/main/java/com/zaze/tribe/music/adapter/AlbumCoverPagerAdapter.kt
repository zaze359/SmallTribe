package com.zaze.tribe.music.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.zaze.tribe.base.CustomFragmentPagerAdapter
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.music.AlbumCoverFragment

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-10-01 - 22:31
 */
class AlbumCoverPagerAdapter(fm: FragmentManager, list: Collection<MusicInfo>) : CustomFragmentPagerAdapter<MusicInfo>(fm, list) {

    override fun getItem(position: Int): Fragment {
        return AlbumCoverFragment.newInstance(dataList[position])
    }
}