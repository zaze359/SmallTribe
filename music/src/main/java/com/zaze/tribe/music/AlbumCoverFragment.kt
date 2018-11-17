package com.zaze.tribe.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaze.tribe.common.BaseFragment
import com.zaze.tribe.music.data.dto.Music
import com.zaze.tribe.music.util.IconCache
import kotlinx.android.synthetic.main.album_cover_frag.*

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-10-01 - 23:26
 */
class AlbumCoverFragment : BaseFragment() {
    private var music: Music? = null

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.album_cover_frag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        music?.let {
            albumCover.setImageBitmap(IconCache.buildMediaIcon(it.data)
                    ?: IconCache.getDefaultMediaIcon())
        }
    }
}