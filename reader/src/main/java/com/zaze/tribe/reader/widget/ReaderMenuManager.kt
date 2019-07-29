package com.zaze.tribe.reader.widget

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.zaze.tribe.reader.R

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-07-26 - 16:20
 */
class ReaderMenuManager(context: Context, private val pageLoader: PageLoader) {
    private val popupWindow: PopupWindow
    private val readerMenuNext: TextView
    private val readerMenuPre: TextView
    private val readerNavBack: TextView

    init {
        val view = View.inflate(context, R.layout.reader_menu_view, null)
        readerMenuNext = view.findViewById(R.id.readerMenuNext)
        readerMenuPre = view.findViewById(R.id.readerMenuPre)
        readerNavBack = view.findViewById(R.id.readerNavBack)
        //
        readerMenuNext.setOnClickListener {
            pageLoader.loadNextChapter()
        }
        readerMenuPre.setOnClickListener {
            pageLoader.loadPreChapter()
        }
        //
        readerNavBack.setOnClickListener {
            dismiss()
            if (context is Activity) {
                context.finish()
            }
        }
        //
        popupWindow = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_corner_stroke_gray))
        popupWindow.isOutsideTouchable = true
        view.setOnClickListener {
            dismiss()
        }
        popupWindow.update()
    }

    fun setTitle(title: String) {
        readerNavBack.text = title
    }

    /**
     * 显示
     * @param anchor 定位的控件
     */
    fun show(anchor: View) {
        if (!popupWindow.isShowing) {
            popupWindow.showAtLocation(anchor, Gravity.CENTER, 0, 0)
        }
    }

    /**
     * 移除popupWindow
     */
    fun dismiss() {
        if (popupWindow.isShowing) {
            popupWindow.dismiss()
        }
    }
}