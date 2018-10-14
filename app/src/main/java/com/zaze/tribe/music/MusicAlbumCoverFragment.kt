package com.zaze.tribe.music

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaze.tribe.R
import com.zaze.tribe.databinding.MusicAlbumCoverFragBinding
import kotlinx.android.synthetic.main.music_album_cover_frag.*
import java.util.ArrayList

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-07-05 - 23:25
 */
class MusicAlbumCoverFragment : Fragment() {
    private lateinit var dataBinding: MusicAlbumCoverFragBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.music_album_cover_frag, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager?.let {
            music_album_cover_view_pager.apply {
                adapter = AlbumCoverPagerAdapter(it, ArrayList(0))
                clipToPadding = false
                pageMargin = 12
                addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {
                    }

                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    }

                    override fun onPageSelected(position: Int) {
                    }
                })
            }
        }
    }
}