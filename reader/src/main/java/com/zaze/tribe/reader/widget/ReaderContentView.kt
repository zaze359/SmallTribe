package com.zaze.tribe.reader.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Scroller
import com.zaze.tribe.reader.bean.BookLine
import com.zaze.utils.log.ZLog

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-20 - 22:40
 */
class ReaderContentView : View, OnConfigurationChangedListener, GestureDetector.OnGestureListener {
    companion object {
        private const val TAG = "ReaderContentView"
    }

    var pageLoader: PageLoader? = null
    private var gestureDetector: GestureDetector = GestureDetector(context, this)

    private var textPaint = Paint()
    private var readerConfiguration = ReaderConfiguration()
    /**
     * 最大行数
     */
    var maxLines: Int = 0

    /**
     * 一行最小字符数
     */
    var minCharSize: Int = 1
    var textWidth = 0F

    private var viewPaddingWidth: Float = 0F
    private val lines = ArrayList<BookLine>()

    /**
     * 当前显示的第一行实际对应位置
     */
    private var selectedLineIndex = 0

    private var moveOffset = 0F

    /**
     * 是否处于拖拽状态
     */
    private var isDragging = false

    constructor(context: Context) : super(context) {
        init(context)

    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        textPaint = readerConfiguration.createReaderContentPaint(context)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.TRANSPARENT)
        var y = 0F
        drawLine(canvas, viewPaddingWidth, y, width - viewPaddingWidth, y)
        lines.forEach {
            y += readerConfiguration.fontHeight
            if (it.isFirstLine) {
                y += readerConfiguration.borderLinePadding
            }
            canvas.drawText(it.content, viewPaddingWidth, y, textPaint)
            drawLine(canvas, viewPaddingWidth, y, width - viewPaddingWidth, y)
            if (it.isLastLine) {
                y += readerConfiguration.borderLinePadding
                drawLine(canvas, viewPaddingWidth, y, width - viewPaddingWidth, y)
            }
        }
        drawLine(canvas, 0f, 0f, width * 1f, height * 1f)
        drawLine(canvas, width * 1f, 0f, 0f, height * 1f)
    }

    private fun drawLine(canvas: Canvas, startX: Float, startY: Float, stopX: Float, stopY: Float) {
//        canvas.drawLine(startX, startY, stopX, stopY, textPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if (textWidth <= 0F) {
            reMeasureTextWidth()
        }
        maxLines = (height / readerConfiguration.fontHeight).toInt()
        viewPaddingWidth = Math.max(14f, (width % textWidth / 2))
        // 首先按照每个字最大宽度计算一行多少个字
        minCharSize = ((width - viewPaddingWidth * 2) / textWidth).toInt()
        // --------------------------------------------------
//        ZLog.i(TAG, "-----------------------------------")
//        if (lines.isNotEmpty()) {
//            ZLog.i(TAG, "lines : ${lines[0]}")
//        }
//        ZLog.i(TAG, "maxLines : $maxLines")
//        ZLog.i(TAG, "textWidth : $textWidth")
//        ZLog.i(TAG, "viewPaddingWidth : $viewPaddingWidth")
//        ZLog.i(TAG, "minCharSize : $minCharSize")
//        ZLog.i(TAG, "-----------------------------------")
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
        textPaint = readerConfiguration.createReaderContentPaint(context)
        reMeasureTextWidth()
    }

    private fun reMeasureTextWidth() {
        textWidth = textPaint.measureText("赵")
    }

    fun measureTextWidth(chars: CharArray): Int {
        var textWidth = viewPaddingWidth * 2
        var length = minCharSize
        if (chars.size <= length) {
            return chars.size
        } else {
            var charWidth: Float
            textWidth += textPaint.measureText(String(chars.copyOfRange(0, length)))
            // 根据差值计算还至少需要多少个字符
            val minFill = calculateMinCharSize(width - textWidth)
            if (chars.size <= length + minFill) {
                return chars.size
            }
            // >=2 才有重新计算的意义
            if (minFill >= 2) {
                textWidth += textPaint.measureText(String(chars.copyOfRange(length, length + minFill)))
                length += minFill
            }
            // 逐字补充
            for (i in length until chars.size) {
                if (textWidth >= width) {
                    break
                }
                charWidth = textPaint.measureText(String(charArrayOf(chars[i])))
                if ((textWidth + charWidth) <= width) {
                    length++
                    textWidth += charWidth
                } else {
                    break
                }
            }
            return length
        }
    }

    private fun calculateMinCharSize(width: Float): Int {
        return (width / textWidth).toInt()
    }

    // ------------------------------------------------------
    override fun onTouchEvent(event: MotionEvent): Boolean {
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
//                }//            }

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