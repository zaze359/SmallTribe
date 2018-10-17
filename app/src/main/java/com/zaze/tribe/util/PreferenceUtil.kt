package com.zaze.tribe.util

import com.zaze.tribe.R
import com.zaze.utils.ZSharedPrefUtil

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-10-17 - 23:31
 */
object PreferenceUtil {

    private const val LATELY_PAGE = "lately_page"

    @JvmStatic
    fun getLatelyPage(): Int {
        return ZSharedPrefUtil.get(LATELY_PAGE, R.id.action_home)
    }

    @JvmStatic
    fun saveLatelyPage(pageId: Int) {
        ZSharedPrefUtil.apply(LATELY_PAGE, pageId)
    }
}