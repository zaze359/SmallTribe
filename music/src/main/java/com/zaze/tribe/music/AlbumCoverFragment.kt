package com.zaze.tribe.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaze.tribe.common.base.AbsFragment
import com.zaze.tribe.music.data.dto.Music
import com.zaze.tribe.music.databinding.MusicAlbumCoverItemFragBinding
import com.zaze.tribe.music.util.MediaIconCache

/**
 * Description : 单个唱片封面页
 *
 * @author : ZAZE
 * @version : 2018-10-01 - 23:26
 */
class AlbumCoverFragment : AbsFragment() {
    private var music: Music? = null
    private var _binding: MusicAlbumCoverItemFragBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(music: Music): AlbumCoverFragment {
            val args = Bundle()
            val fragment = AlbumCoverFragment()
            args.putParcelable("music", music)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        music = arguments?.getParcelable("music")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MusicAlbumCoverItemFragBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        music?.let {
            binding.musicAlbumCover.setImageBitmap(
                MediaIconCache.buildMediaIcon(it.data)
                    ?: MediaIconCache.getDefaultMediaIcon()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}