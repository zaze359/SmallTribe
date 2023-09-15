package com.zaze.tribe.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.zaze.tribe.common.base.AbsFragment
import com.zaze.tribe.music.adapter.AlbumCoverPagerAdapter
import com.zaze.tribe.music.databinding.MusicAlbumCoverPagerFragBinding

/**
 * Description : 歌曲封面分页页
 *
 * @author : ZAZE
 * @version : 2018-07-05 - 23:25
 */
class AlbumCoverPagerFragment : AbsFragment(), ViewPager.OnPageChangeListener {
    private var _binding: MusicAlbumCoverPagerFragBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MusicAlbumCoverPagerFragBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.let {
            binding.musicAlbumCoverPager.apply {
                adapter = AlbumCoverPagerAdapter(it, MusicPlayerRemote.getPlayingQueue())
                clipToPadding = false
                pageMargin = 12
                currentItem = MusicPlayerRemote.getPosition()
                addOnPageChangeListener(this@AlbumCoverPagerFragment)
            }
        }
        MusicPlayerRemote.curMusicData.observe(viewLifecycleOwner, Observer {
            binding.musicAlbumCoverPager.setCurrentItem(MusicPlayerRemote.getPosition(), true)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.musicAlbumCoverPager.removeOnPageChangeListener(this)
        _binding = null
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        MusicPlayerRemote.playAt(position)
    }
}