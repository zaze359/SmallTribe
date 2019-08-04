package com.zaze.tribe.reader

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zaze.tribe.common.BaseRecyclerAdapter
import com.zaze.tribe.reader.bean.BookChapter

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-08-04 - 16:47
 */
class ReaderCatalogAdapter(context: Context, chapters: Collection<BookChapter>) : BaseRecyclerAdapter<BookChapter, ReaderCatalogAdapter.CatalogHolder>(context, chapters) {

    private var selectedItem = 0

    override fun getViewLayoutId(): Int {
        return R.layout.reader_catalog_item
    }

    override fun createViewHolder(convertView: View): CatalogHolder {
        return CatalogHolder(convertView)
    }

    override fun onBindView(holder: CatalogHolder, value: BookChapter, position: Int) {
        if (selectedItem == position) {
            holder.catalogItemName.setTextColor(ContextCompat.getColor(context, R.color.orange))
        } else {
            holder.catalogItemName.setTextColor(ContextCompat.getColor(context, R.color.black))
        }
        holder.catalogItemName.text = value.chapter
    }

    fun scrollTo(position: Int) {
        selectedItem = position
        notifyDataSetChanged()
    }

    class CatalogHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val catalogItemName = itemView.findViewById<TextView>(R.id.catalogItemName)
    }

}