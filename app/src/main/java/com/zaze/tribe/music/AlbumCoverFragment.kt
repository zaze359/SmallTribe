package com.zaze.tribe.music

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaze.tribe.R
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.util.IconCache
import kotlinx.android.synthetic.main.album_cover_frag.*

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-10-01 - 23:26
 */
class AlbumCoverFragment : Fragment() {
    private var musicInfo: MusicInfo? = null

    companion object {
        fun newInstance(musicInfo: MusicInfo): AlbumCoverFragment {
            val args = Bundle()
            val fragment = AlbumCoverFragment()
            args.putParcelable("musicInfo", musicInfo)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        musicInfo = arguments?.getParcelable("musicInfo")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.album_cover_frag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        musicInfo?.let {
            album_cover_iv.setImageBitmap(IconCache.buildMediaIcon(it.data))
        }
    }
}