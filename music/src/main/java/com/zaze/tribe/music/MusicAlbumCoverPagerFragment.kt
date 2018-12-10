package com.zaze.tribe.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.zaze.tribe.common.BaseFragment
import com.zaze.tribe.music.adapter.AlbumCoverPagerAdapter
import kotlinx.android.synthetic.main.music_album_cover_pager_frag.*

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-07-05 - 23:25
 */
class MusicAlbumCoverPagerFragment : BaseFragment(), ViewPager.OnPageChangeListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.music_album_cover_pager_frag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager?.let {
            musicAlbumCoverPager.apply {
                adapter = AlbumCoverPagerAdapter(it, MusicPlayerRemote.getPlayingQueue())
                clipToPadding = false
                pageMargin = 12
                currentItem = MusicPlayerRemote.getPosition()
                addOnPageChangeListener(this@MusicAlbumCoverPagerFragment)
            }
        }
        MusicPlayerRemote.curMusicData.observe(this, Observer {
            musicAlbumCoverPager.setCurrentItem(MusicPlayerRemote.getPosition(), true)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        musicAlbumCoverPager.removeOnPageChangeListener(this)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        MusicPlayerRemote.playAt(position)
    }
}