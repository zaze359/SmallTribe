package com.zaze.tribe.common.base

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ArrayRes
import androidx.annotation.DimenRes
import com.zaze.core.designsystem.skin.SkinLayoutInflaterFactory

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-30 - 15:48
 */
abstract class AbsActivity : AbsThemeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutInflater.factory2 =
            SkinLayoutInflaterFactory { parent: View?, name: String?, context: Context, attrs: AttributeSet ->
                delegate.createView(parent, name, context, attrs)
            }
        super.onCreate(savedInstanceState)
    }

    // --------------------------------------------------
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
    // --------------------------------------------------
}
