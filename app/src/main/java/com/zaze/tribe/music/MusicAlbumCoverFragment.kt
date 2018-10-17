package com.zaze.tribe.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.zaze.tribe.R
import com.zaze.tribe.base.BaseFragment
import kotlinx.android.synthetic.main.music_album_cover_frag.*

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-07-05 - 23:25
 */
class MusicAlbumCoverFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.music_album_cover_frag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager?.let {
            music_album_cover_view_pager.apply {
                adapter = AlbumCoverPagerAdapter(it, MusicPlayerRemote.playerList)
                clipToPadding = false
                pageMargin = 12
                currentItem = MusicPlayerRemote.getCurPosition()
                addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {
                    }

                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    }

                    override fun onPageSelected(position: Int) {
                        MusicPlayerRemote.startAt(position)
                    }
                })
            }
        }
    }
}