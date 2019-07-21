package com.zaze.tribe.reader.bookshelf

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.zaze.tribe.common.BaseRecyclerAdapter
import com.zaze.tribe.reader.R
import com.zaze.tribe.reader.ReaderActivity
import com.zaze.tribe.reader.bean.Book

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-06-08 - 0:43
 */
class BookshelfAdapter(context: Context, data: Collection<Book>)
    : BaseRecyclerAdapter<Book, BookshelfAdapter.BookshelfHolder>(context, data) {
    override fun getViewLayoutId(): Int {
        return R.layout.bookshelf_item
    }

    override fun createViewHolder(convertView: View): BookshelfHolder {
        return BookshelfHolder(convertView)
    }

    override fun onBindView(holder: BookshelfHolder, value: Book, position: Int) {
        holder.bookCover.setImageBitmap(BookIconCache.getBookCover(value))
        holder.itemView.setOnClickListener {
            ReaderActivity.reader(context, value.localPath)
        }
    }

    class BookshelfHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookCover: ImageView = itemView.findViewById(R.id.bookCover)
    }
}