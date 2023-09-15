package com.zaze.tribe.common.base

import androidx.annotation.ArrayRes
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import com.zaze.tribe.common.util.hideKeyboard
import com.zaze.utils.ToastUtil

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-11-30 - 00:00
 */
abstract class AbsFragment : AbsPermissionFragment {
    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)


    override fun onPause() {
        super.onPause()
        view.hideKeyboard()
    }

    override fun onDestroyView() {
        view.hideKeyboard()
        super.onDestroyView()
    }

    fun showToast(resId: Int) {
        showToast(getString(resId))
    }

    fun showToast(content: String?) {
        ToastUtil.toast(context, content)
    }

    // --------------------------------------------------

    /**
     * 读取dimen 转 px
     */
    fun getDimen(@DimenRes resId: Int): Int {
        return this.resources.getDimensionPixelSize(resId)
    }

    /**
     * arrays.xml 转数据
     */
    fun getStringArray(@ArrayRes resId: Int): Array<String> {
        return this.resources.getStringArray(resId)
    }

}