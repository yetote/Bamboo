package com.example.bamboo.util;

import android.media.MediaCodecList;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class describe
 * @time 2018/12/13 15:30
 * @change
 * @chang time
 * @class describe
 */
public class DecodeUtil {
    private static Map<String, String> codecMap = new HashMap<>();

    static {
        codecMap.put("h264", "video/avc");
    }

    private static String findHardwareCodec(String ffmpegCodeName) {
        if (codecMap.containsKey(ffmpegCodeName)) {
            return codecMap.get(ffmpegCodeName);
        }
        return "";
    }

    public static boolean isSupportCodec(String type) {
        int count = MediaCodecList.getCodecCount();
        for (int i = 0; i < count; i++) {
            String[] types = MediaCodecList.getCodecInfoAt(i).getSupportedTypes();
            for (int j = 0; j < types.length; j++) {
                if (types[j].equals(findHardwareCodec(type))) {
                    return true;
                }
            }
        }
        return false;
    }
}
