package com.zaze.tribe.music.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.zaze.tribe.common.CustomFragmentPagerAdapter
import com.zaze.tribe.music.data.dto.Music
import com.zaze.tribe.music.AlbumCoverFragment

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-10-01 - 22:31
 */
class AlbumCoverPagerAdapter(fm: FragmentManager, list: Collection<Music>) : CustomFragmentPagerAdapter<Music>(fm, list) {

    override fun getItem(position: Int): Fragment {
        return AlbumCoverFragment.newInstance(dataList[position])
    }
}