package com.zaze.tribe.music.util;

import android.text.TextUtils;

import com.zaze.tribe.music.data.entity.LrcLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-09-30 - 0:38
 */
public class LrcProcessor {
    /**
     * 处理歌词文件
     *
     * @param inputStream
     * @return 歌词的时间点（转换为毫秒数）和内容
     */
    public static ArrayList<LrcLine> process(InputStream inputStream) {
        ArrayList<LrcLine> lrcLines = new ArrayList<>();
        // 存放歌词信息
        try {
            InputStreamReader inputReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bf = new BufferedReader(inputReader);
            // 创建一个正则表达式对象 寻找两边都带中括号的字符串
            Pattern p = Pattern.compile("\\[([^\\]]+)\\]");
            String temp = null;
            long timeMill;
            String content;
            while ((temp = bf.readLine()) != null) {
                Matcher m = p.matcher(temp);
                if (m.find()) {
                    // 返回匹配正则表达式的文本, 赋值给timeStr [00.01.20]
                    String timeStr = m.group();
                    //得到时间毫秒数
                    timeMill = time2Long(timeStr.substring(1, timeStr.length() - 1));
                    //得到歌词信息部分
                    content = temp.substring(timeStr.length());
                    if (!TextUtils.isEmpty(content)) {
                        LrcLine lrcLine = new LrcLine();
                        lrcLine.setTimeMillis(timeMill);
                        lrcLine.setContent(content.trim());
                        lrcLine.setIndex(lrcLines.size());
                        lrcLines.add(lrcLine);
                    }
                }
            }
//            if (!TextUtils.isEmpty(result)) {
//                com.zaze.tribe.music.data.entity.LrcLine endLrcLine = new com.zaze.tribe.music.data.entity.LrcLine();
//                endLrcLine.setContent(result);
//                endLrcLine.setTimeMillis(timeMill);
//                endLrcLine.setIndex(index);
//                /* 加入到队列中
//                 * Queue中 offer() 和 add() 的区别在于 add()失败时会抛出异常
//                 * offer()则用返回值来判断成功与否
//                 */
//                LrcLines.add(endLrcLine);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lrcLines;
    }

    /**
     * 将分钟,秒 转换为毫秒 L
     *
     * @param timeStr
     * @return
     */
    private static long time2Long(String timeStr) {
        try {
            String s[] = timeStr.split(":");
            int min = Integer.parseInt(s[0]);
            String ss[] = s[1].split("\\.");
            int second = Integer.parseInt(ss[0]);
            int mill = Integer.parseInt(ss[1]);
            return (min * 60 + second) * 1000L + mill * 10;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
