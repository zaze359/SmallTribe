package com.zaze.tribe.reader.data.loader

import org.junit.Test

import org.junit.Assert.*

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-21 - 23:42
 */
class TxtFileLoaderTest {
    val txtFileLoader  = TxtFileLoader()

    @Test
    fun loadFile() {
    }

    @Test
    fun matchChapter() {
        assertNotNull(txtFileLoader.matchChapter("第一部"))
        assertNotNull(txtFileLoader.matchChapter(" 第二章"))
        assertNotNull(txtFileLoader.matchChapter("第一部 紫川三杰"))
        assertNotNull(txtFileLoader.matchChapter("第1章 - 居然会赢"))
        assertNotNull(txtFileLoader.matchChapter("第12345章 - 居然会赢"))
        assertNotNull(txtFileLoader.matchChapter("第一万二千三百四十五章 - 居然会赢"))
        assertNotNull(txtFileLoader.matchChapter("   第一千一百零三章 - 居然会赢"))
        assertNull(txtFileLoader.matchChapter("“是谁说魔族军队很强的？”罗杰副旗本（旗本：家族职衔名称）得意的望着山脚下如同潮水般溃退的魔族精锐部队，“看起来似乎我还更强上一点。”"))
    }
}