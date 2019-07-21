package com.zaze.tribe.reader.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.zaze.tribe.reader.R

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-20 - 22:24
 */
class ReaderView : LinearLayout {

    private lateinit var readerTop: ReaderTopView
    private lateinit var readerCenter: ReaderCenterView
    private lateinit var readerBottom: ReaderBottomView

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
    }

    override fun requestLayout() {
        super.requestLayout()
    }

    private fun init(context: Context?) {
        val reader = inflate(context, R.layout.reader_view, this)
        readerTop = reader.findViewById(R.id.bookReaderTop)
        readerCenter = reader.findViewById(R.id.bookReaderCenter)
        readerBottom = reader.findViewById(R.id.bookReaderBottom)
    }
}