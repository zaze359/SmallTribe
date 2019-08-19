package com.zaze.tribe.reader.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.zaze.tribe.reader.R
import com.zaze.tribe.reader.bean.ReaderBook
import com.zaze.tribe.reader.bean.ReaderPage

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-20 - 22:24
 */
class ReaderView : LinearLayout, OnConfigurationChangedListener, PageLoader {
    private var readerBook: ReaderBook? = null
    private var readerConfiguration: ReaderConfiguration

    private var readerChapter: TextView
    private var readerContent: ReaderContentView
    private lateinit var readerProgress: TextView
    // ------------------------------------------------------

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    init {
        val reader = inflate(context, R.layout.reader_view, this)
        readerConfiguration = ReaderConfiguration()
        readerChapter = reader.findViewById(R.id.readerChapter)
        readerContent = reader.findViewById(R.id.bookReaderContent)
        readerContent.pageLoader = this
        readerContent.onConfigurationChanged(readerConfiguration)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }


    override fun onConfigurationChanged(readerConfiguration: ReaderConfiguration) {
        this.readerConfiguration = readerConfiguration
        readerContent.onConfigurationChanged(readerConfiguration)
        invalidate()
    }

    fun startReadBook(readerBook: ReaderBook) {
        this.readerBook = readerBook
        readerBook.loadFromHistory(this)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        readerContent.setOnClickListener(l)
    }

    override fun loadNextPage() {
        readerBook?.loadNextPage(this)
    }

    override fun loadPrePage() {
        readerBook?.loadPrePage(this)
    }

    override fun loadNextChapter() {
        readerBook?.loadNextChapter(this)
    }

    override fun loadPreChapter() {
        readerBook?.loadPreChapter(this)
    }

    override fun loadChapter(chapterIndex: Int) {
        readerBook?.loadChapter(this, chapterIndex)
    }

    override fun onLoaded(readerPage: ReaderPage) {
        readerChapter.text = readerPage.chapter
        readerContent.load(readerPage.lines)
        invalidate()
    }

    override fun measureTextWidth(chars: CharArray): Int {
        return readerContent.measureTextWidth(chars)
    }

    override fun hasMoreSpace(lineSize: Int, borderLineSize: Int): Boolean {
        return lineSize * readerConfiguration.fontHeight + borderLineSize * readerConfiguration.borderLinePadding <
                (readerContent.height - readerConfiguration.fontHeight)
    }
}