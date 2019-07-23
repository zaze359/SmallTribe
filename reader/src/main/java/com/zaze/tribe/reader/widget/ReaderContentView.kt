package com.zaze.tribe.reader.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.zaze.tribe.reader.R
import com.zaze.tribe.reader.bean.BookLine
import com.zaze.tribe.reader.bean.BookParagraph
import com.zaze.utils.ZDisplayUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-20 - 22:40
 */
class ReaderContentView : View {
    /**
     * 最大行数
     */
    private var maxLines: Int = 0
    private var viewPaddingHeight: Float = 0F
    private var viewPaddingWidth: Float = 0F
    //
    private val lines = ArrayList<BookLine>()
    /**
     * 当前显示的第一行实际对应位置
     */
    private var selectedLineIndex = 0
    //
    private val paint: Paint
    private var fontHeight: Float

    private var moveOffset = 0F

    private var baseX = 0f
    private var baseY = 0f
    private var offsetCount = 0

    /**
     * 当前触摸的 y
     */
    private var touchY = 0F

    private val DEFAULT_ALPHA = 230

    /**
     * 是否处于拖拽状态
     */
    private var isDragging = false


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        paint = createPaint().also {
            it.textSize = 24.0f
            it.color = ContextCompat.getColor(context, R.color.colorPrimary)
            it.alpha = 255
        }
        fontHeight = paint.textSize + 2
    }

    private fun createPaint(): Paint {
        return Paint().apply {
            isDither = true
            isAntiAlias = true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.TRANSPARENT)
        var y = viewPaddingHeight
        lines.forEach {
            y += fontHeight
            canvas.drawText(it.content, viewPaddingWidth, y, paint)
        }
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
        viewPaddingWidth = Math.max(2f, (width % paint.textSize / 2))
        setMeasuredDimension(width, height)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchY = event.y
                isDragging = true
            }
            MotionEvent.ACTION_MOVE -> {
                moveOffset = event.y - touchY
                when {
                    moveOffset >= fontHeight -> {
                        // 下移
                        touchY = event.y
                        moveDown()
                    }
                    moveOffset <= -fontHeight -> {
                        // 上移
                        touchY = event.y
                        moveUp()
                    }
                    else -> invalidate()
                }
            }
            else -> {
                isDragging = false
            }
        }
        return true
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

    fun load(paragraphs: List<BookParagraph>) {
        for (paragraph in paragraphs) {
            ZLog.d(ZTag.TAG_DEBUG, paragraph.paragraph)
            var chars = paragraph.paragraph.toCharArray()
            if (lines.size >= maxLines) {
                break
            } else {
                while (chars.isNotEmpty()) {
                    val index = measureTextWidth(chars)
                    lines.add(BookLine(String(chars.copyOfRange(0, index))))
                    chars = chars.copyOfRange(index, chars.size)
                }
            }
        }
        invalidate()
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
}