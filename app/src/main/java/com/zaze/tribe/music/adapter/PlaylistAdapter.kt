package com.zaze.tribe.music.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zaze.tribe.R
import com.zaze.tribe.base.BaseRecyclerAdapter
import com.zaze.tribe.data.dto.Music
import com.zaze.tribe.databinding.PlaylistItemBinding
import com.zaze.tribe.music.MusicPlayerRemote
import com.zaze.tribe.music.vm.PlaylistViewModel


/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-10 - 10:26
 */
class PlaylistAdapter(
        context: Context,
        data: Collection<Music>,
        private val viewModel: PlaylistViewModel
) : BaseRecyclerAdapter<Music, PlaylistAdapter.MusicViewHolder>(context, data) {
    override fun getViewLayoutId(): Int {
        return R.layout.playlist_item
    }

    override fun createViewHolder(convertView: View): MusicViewHolder {
        return MusicViewHolder(convertView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val binding = PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.viewModel = viewModel
        return this.createViewHolder(binding.root.apply { tag = viewType.toString() })
    }

    override fun onBindView(holder: MusicViewHolder, value: Music, position: Int) {
        val binding = DataBindingUtil.getBinding<PlaylistItemBinding>(holder.itemView)
        binding?.music = value
        binding?.root?.setOnClickListener {
            MusicPlayerRemote.playAt(position)
        }
    }

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}