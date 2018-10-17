package com.zaze.tribe.music

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zaze.tribe.R
import com.zaze.tribe.base.BaseRecyclerAdapter
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
        private val viewModel: MusicListViewModel
) : BaseRecyclerAdapter<MusicInfo, MusicAdapter.MusicViewHolder>(context, data) {
    override fun getViewLayoutId(): Int {
        return R.layout.music_item
    }

    override fun createViewHolder(convertView: View): MusicViewHolder {
        return MusicViewHolder(convertView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val binding = MusicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.viewModel = viewModel
        return this.createViewHolder(binding.root.apply { tag = viewType.toString() })
    }

    override fun onBindView(holder: MusicViewHolder, value: MusicInfo?, position: Int) {
        value?.let {
            ZLog.i(ZTag.TAG_DEBUG, "${value.title}:${value.data}")
            val binding = DataBindingUtil.getBinding<MusicItemBinding>(holder.itemView)
            binding?.music = value
        }
    }

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}