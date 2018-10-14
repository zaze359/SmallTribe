package com.zaze.tribe.music

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaze.common.adapter.BaseRecyclerAdapter
import com.zaze.tribe.R
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.tribe.databinding.MusicItemBinding
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag


/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-10 - 10:26
 */
class MusicAdapter(
        context: Context,
        data: Collection<MusicInfo>,
        private val viewModel: MusicViewModel
) : BaseRecyclerAdapter<MusicInfo, MusicAdapter.MusicViewHolder>(context, data) {
    override fun getViewLayoutId(): Int {
        return R.layout.music_item
    }

    override fun createViewHolder(p0: View): MusicViewHolder {
        return MusicViewHolder(p0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val binding = MusicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.viewModel = viewModel
        return this.createViewHolder(binding.root)
    }

    override fun onBindView(viewHolder: MusicViewHolder, music: MusicInfo, p2: Int) {
        val binding = DataBindingUtil.getBinding<MusicItemBinding>(viewHolder.itemView)
        ZLog.i(ZTag.TAG_DEBUG, "${music.title}:${music.data}")
        binding.music = music
    }

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}