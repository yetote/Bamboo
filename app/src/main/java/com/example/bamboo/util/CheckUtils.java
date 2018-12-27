package com.example.bamboo.util;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class 检查类
 * @time 2018/12/26 13:30
 * @change
 * @chang time
 * @class describe
 */
public class CheckUtils {
    /**
     * 检查输入字符串是否为空
     *
     * @param s 要检查的字符串
     * @return 是否为空
     */
    public static boolean checkNull(String... s) {
        for (String value : s) {
            if (value == null || "".equals(value)) {
                return true;
            }
        }
        return false;
    }
}
