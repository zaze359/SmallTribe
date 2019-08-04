package com.zaze.tribe.reader.widget

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zaze.tribe.reader.R

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-07-26 - 16:20
 */
class ReaderMenuManager(context: Context, private val pageLoader: PageLoader) : DrawerLayout.DrawerListener {

    private val popupWindow: PopupWindow
    private val readerNavNext: TextView
    private val readerNavPre: TextView
    private val readerNavBack: TextView
    private val readerNavMenu: BottomNavigationView
    var readerMenuListener: ReaderMenuListener? = null

    init {
        val view = View.inflate(context, R.layout.reader_menu_view, null)
        readerNavNext = view.findViewById(R.id.readerNavNext)
        readerNavPre = view.findViewById(R.id.readerNavPre)
        readerNavBack = view.findViewById(R.id.readerNavBack)
        readerNavMenu = view.findViewById(R.id.readerNavMenu)
        //
        readerNavNext.setOnClickListener {
            pageLoader.loadNextChapter()
        }
        readerNavPre.setOnClickListener {
            pageLoader.loadPreChapter()
        }
        readerNavBack.setOnClickListener {
            dismiss()
            if (context is Activity) {
                context.finish()
            }
        }
        //
        readerNavMenu.itemTextColor = context.resources.getColorStateList(R.color.white)
        readerNavMenu.itemIconTintList = context.resources.getColorStateList(R.color.white)

        readerNavMenu.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_catalog -> {
                    readerMenuListener?.showCatalog()
                    dismiss()
                }
                else -> {
                }
            }
            true
        }
        //
        popupWindow = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        popupWindow.contentView.setOnClickListener {
            dismiss()
        }
        popupWindow.setOnDismissListener {
            readerMenuListener?.onDismiss()
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

    // --------------------------------------------------

    override fun onDrawerStateChanged(newState: Int) {
        // 状态改变时
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        // 滑动时
    }

    override fun onDrawerClosed(drawerView: View) {
        // 完全关闭时
    }

    override fun onDrawerOpened(drawerView: View) {
        // 完全展开时
    }

    // --------------------------------------------------
    interface ReaderMenuListener : PopupWindow.OnDismissListener {
        fun showCatalog()
    }
}