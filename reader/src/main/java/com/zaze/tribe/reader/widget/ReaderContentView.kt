package com.zaze.tribe.reader.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.zaze.tribe.reader.bean.BookLine
import com.zaze.utils.log.ZLog

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-20 - 22:40
 */
class ReaderContentView : View, OnConfigurationChangedListener, GestureDetector.OnGestureListener {
    var pageLoader: PageLoader? = null
    private var gestureDetector: GestureDetector = GestureDetector(context, this)

    /**
     * 最大行数
     */
    var maxLines: Int = 0
    private var viewPaddingHeight: Float = 0F
    private var viewPaddingWidth: Float = 0F
    //
    private val lines = ArrayList<BookLine>()
    /**
     * 当前显示的第一行实际对应位置
     */
    private var selectedLineIndex = 0
    //
    var paint = Paint()
    private var fontHeight: Float = 22F

    private var moveOffset = 0F

    private var baseX = 0f
    private var baseY = 0f
    private var offsetCount = 0

    /**
     * 当前触摸的 y
     */
    private var touchY = 0F

    /**
     * 是否处于拖拽状态
     */
    private var isDragging = false


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.TRANSPARENT)
        var y = viewPaddingHeight
        canvas.drawLine(viewPaddingWidth, y, width - viewPaddingWidth, y, paint)
        lines.forEach {
            y += fontHeight
            canvas.drawText(it.content, viewPaddingWidth, y, paint)
            canvas.drawLine(viewPaddingWidth, y, width - viewPaddingWidth, y, paint)
        }
        canvas.drawLine(0f, 0f, width * 1f, height * 1f, paint)
        canvas.drawLine(width * 1f, 0f, 0f, height * 1f, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        baseX = width * 0.5f
        baseY = height * 0.5f
        offsetCount = (baseY / fontHeight).toInt()
        maxLines = (height / fontHeight).toInt()
        viewPaddingHeight = Math.max(4f, (height - fontHeight * maxLines) / 2)
        viewPaddingWidth = Math.max(12f, (width % paint.textSize / 2))
        setMeasuredDimension(width, height)
    }

    private fun moveUp() {
        moveOffset = 0F
        selectedLineIndex = Math.min(lines.size - 1, selectedLineIndex + 1)
        invalidate()
    }

    private fun moveDown() {
        moveOffset = 0F
        selectedLineIndex = Math.max(0, selectedLineIndex - 1)
        invalidate()
    }


    // --------------------------------------------------
    fun next() {
        if (!isDragging) {
            moveUp()
        }
    }

    fun load(bookLines: List<BookLine>) {
        lines.clear()
        lines.addAll(bookLines)
        invalidate()
    }

    override fun onConfigurationChanged(readerConfiguration: ReaderConfiguration) {
        paint = readerConfiguration.createReaderContentPaint(context)
        fontHeight = readerConfiguration.fontHeight
    }

    fun measureTextWidth(chars: CharArray): Int {
        var textWidth = viewPaddingWidth * 2
        var charWidth: Float
        var length = 0
        for (char in chars) {
            charWidth = paint.measureText(String(charArrayOf(char)))
            if ((textWidth + charWidth) > width) {
                break
            } else {
                textWidth += charWidth
            }
            length++
        }
        return length
    }

    // ------------------------------------------------------
    override fun onTouchEvent(event: MotionEvent): Boolean {
        ZLog.i("onTouchEvent 2222 ", "${event.x} : ${event.y}")
        return gestureDetector.onTouchEvent(event)
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                touchY = event.y
//                isDragging = true
//            }
//            MotionEvent.ACTION_MOVE -> {
//                moveOffset = event.y - touchY
//                when {
//                    moveOffset >= fontHeight -> {
//                        // 下移
//                        touchY = event.y
//                        moveDown()
//                    }
//                    moveOffset <= -fontHeight -> {
//                        // 上移
//                        touchY = event.y
//                        moveUp()
//                    }
//                    else -> invalidate()
//                }
//            }
//            else -> {
//                isDragging = false
//            }
//        }
//        return true
    }

    override fun onShowPress(e: MotionEvent) {
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return when {
            e.x <= width / 3 -> {
                pageLoader?.loadPrePage()
                true
            }
            e.x >= 2 * width / 3 -> {
                pageLoader?.loadNextPage()
                true
            }
            else -> performClick()
        }
    }

    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {
    }
}