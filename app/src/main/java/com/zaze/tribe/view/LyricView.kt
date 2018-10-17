package com.zaze.tribe.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.zaze.tribe.R
import com.zaze.utils.ZDisplayUtil

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-25 - 20:10
 */
class LyricView : AppCompatTextView {
    var values: List<String>? = null
    private var index = 0

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
    private var isDraging = false


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
            if (index >= 0 && index < it.size) {
                val centerValue = it[index]
                canvas.drawText(centerValue, baseX, baseY, paintH)
            }
            paintL.alpha = DEFAULT_ALPHA
            var aboveY = baseY
            for (i in index - 1 downTo Math.max(0, index - offsetCount)) {
                values?.get(i)?.apply {
                    aboveY -= interval
                    paintL.alpha = Math.max(0, paintL.alpha - 10)
                    if (aboveY >= 0) {
                        canvas.drawText(this, baseX, aboveY, paintL)
                    }
                }
            }
            paintL.alpha = DEFAULT_ALPHA
            var belowY = baseY
            for (i in index + 1 until Math.min(it.size, index + offsetCount)) {
                values?.get(i)?.apply {
                    belowY += interval
                    paintL.alpha = Math.max(0, paintL.alpha - 10)
                    if (belowY <= height) {
                        canvas.drawText(this, baseX, belowY, paintL)
                    }
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
                isDraging = true
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
                isDraging = false
            }
        }
        return true
    }

    private fun moveUp() {
        mOffset = 0F
        index = Math.min(values?.size?.let { it - 1 } ?: 0, index + 1)
        invalidate()
    }

    private fun moveDown() {
        mOffset = 0F
        index = Math.max(0, index - 1)
        invalidate()
    }


    // --------------------------------------------------
    fun next() {
        if (!isDraging) {
            moveUp()
        }
    }
}