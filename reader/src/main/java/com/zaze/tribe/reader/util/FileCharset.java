package com.zaze.tribe.reader.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-22 - 23:41
 */
public class FileCharset {

    /**
     * 获得文件编码
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String getCharset(String filePath) throws Exception {
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(filePath));
        int p = (bin.read() << 8) + bin.read();
        bin.close();
        String code = null;
        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }
        return code;
    }
}
