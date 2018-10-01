package com.zaze.tribe.music

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaze.tribe.data.dto.MusicInfo

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-10-01 - 23:26
 */
class AlbumCoverFragment : Fragment() {

    companion object {
        fun newInstance(musicInfo: MusicInfo): AlbumCoverFragment {
            val args = Bundle()
            val fragment = AlbumCoverFragment()
            args.putSerializable("", musicInfo)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}