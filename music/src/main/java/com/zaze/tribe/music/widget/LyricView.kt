package com.zaze.tribe.music.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.zaze.tribe.music.R
import com.zaze.utils.ZDisplayUtil

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-25 - 20:10
 */
class LyricView : View {
    var values: List<String>? = null
    /**
     * 当前显示的第一行实际对应位置
     */
    private var selectedLineIndex = 0

    private val paintL: Paint
    private val paintH: Paint
    private val interval = ZDisplayUtil.pxFromDp(24f)
    private var mOffset = 0F

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
        paintH = createPaint().also {
            it.textSize = ZDisplayUtil.pxFromDp(22f) * 1.0f
            it.color = ContextCompat.getColor(context, R.color.colorPrimary)
            it.alpha = 255
        }
        paintL = createPaint().also {
            it.textSize = ZDisplayUtil.pxFromDp(18f) * 1.0f
            it.color = ContextCompat.getColor(context, R.color.colorPrimary)
            it.alpha = DEFAULT_ALPHA
        }
    }

    private fun createPaint(): Paint {
        return Paint().apply {
            textAlign = Paint.Align.CENTER
            isDither = true
            isAntiAlias = true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.TRANSPARENT)
        values?.let {
            canvas.translate(0f, mOffset)
            drawSelected(canvas, it, paintH)
            drawAbove(canvas, it, paintL)
            drawBelow(canvas, it, paintL)
        }
    }

    /**
     * 绘制当前选中部分
     */
    private fun drawSelected(canvas: Canvas, textList: List<String>, paint: Paint) {
        if (selectedLineIndex >= 0 && selectedLineIndex < textList.size) {
            val centerValue = textList[selectedLineIndex]
            canvas.drawText(centerValue, baseX, baseY, paint)
        }
    }

    /**
     * 绘制选中部分上方
     */
    private fun drawAbove(canvas: Canvas, textList: List<String>, paint: Paint) {
        paint.alpha = DEFAULT_ALPHA
        var aboveY = baseY
        for (i in selectedLineIndex - 1 downTo Math.max(0, selectedLineIndex - offsetCount)) {
            textList[i].apply {
                aboveY -= interval
                paintL.alpha = Math.max(0, paintL.alpha - 10)
                if (aboveY >= 0) {
                    canvas.drawText(this, baseX, aboveY, paint)
                }
            }
        }
    }


    /**
     * 绘制选中部分下方
     */
    private fun drawBelow(canvas: Canvas, textList: List<String>, paint: Paint) {
        paint.alpha = DEFAULT_ALPHA
        var belowY = baseY
        for (i in selectedLineIndex + 1 until Math.min(textList.size, selectedLineIndex + offsetCount)) {
            textList[i].apply {
                belowY += interval
                paintL.alpha = Math.max(0, paintL.alpha - 10)
                if (belowY <= height) {
                    canvas.drawText(this, baseX, belowY, paint)
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        baseX = width * 0.5f
        baseY = height * 0.5f
        offsetCount = (baseY / interval).toInt()
        setMeasuredDimension(width, height)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchY = event.y
                isDragging = true
            }
            MotionEvent.ACTION_MOVE -> {
                mOffset = event.y - touchY
                when {
                    mOffset >= interval -> {
                        // 下移
                        touchY = event.y
                        moveDown()
                    }
                    mOffset <= -interval -> {
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
        mOffset = 0F
        selectedLineIndex = Math.min(values?.size?.let { it - 1 } ?: 0, selectedLineIndex + 1)
        invalidate()
    }

    private fun moveDown() {
        mOffset = 0F
        selectedLineIndex = Math.max(0, selectedLineIndex - 1)
        invalidate()
    }


    // --------------------------------------------------
    fun next() {
        if (!isDragging) {
            moveUp()
        }
    }
}