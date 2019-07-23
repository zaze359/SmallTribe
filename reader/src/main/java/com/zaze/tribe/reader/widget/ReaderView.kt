package com.zaze.tribe.reader.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.zaze.tribe.reader.R
import com.zaze.tribe.reader.bean.ReaderPage
import com.zaze.tribe.reader.util.ReaderProfile

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-20 - 22:24
 */
class ReaderView : LinearLayout {

    private lateinit var readerChapter: TextView
    private lateinit var readerContent: ReaderContentView
    private lateinit var readerProgress: TextView


    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
        ReaderProfile.init(context, width, height)
    }


    private fun init(context: Context?) {
        val reader = inflate(context, R.layout.reader_view, this)
        readerChapter = reader.findViewById(R.id.readerChapter)
        readerContent = reader.findViewById(R.id.bookReaderCenter)
    }

    fun loadReaderPage(page: ReaderPage?) {
        page?.let {
            readerChapter.text = it.chapter.chapter
            readerContent.load(page.maxParagraphs)
        }
    }
}