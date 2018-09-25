package com.zaze.tribe.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class WidthFitSquareLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var forceSquare = true
    set(value)  {
        field = value
        requestLayout()
    }

//    constructor(context: Context) : super(context) {}
//
//    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {}
//
//    constructor(context: Context, attributeSet: AttributeSet, i: Int) : super(context, attributeSet, i) {}
//
//    @TargetApi(21)
//    constructor(context: Context, attributeSet: AttributeSet, i: Int, i2: Int) : super(context, attributeSet, i, i2) {
//    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if(forceSquare) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}
