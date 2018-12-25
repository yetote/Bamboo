package com.example.bamboo.util;

import com.example.bamboo.R;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class describe
 * @time 2018/12/25 16:13
 * @change
 * @chang time
 * @class describe
 */
public class IdentityUtils {
    private static Map<String, Integer> identityMap;

    static {
        identityMap = new HashMap<>();
        identityMap.put("star", R.drawable.star);
        identityMap.put("vip1", R.drawable.lv_one);
        identityMap.put("vip2", R.drawable.lv_two);
        identityMap.put("vip3", R.drawable.lv_three);

    }

    public static int getIdentityDrawable(String identity) {
        return identityMap.get(identity);
    }
}
