package com.example.bamboo.util;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

/**
 * @author yetote QQ:503779938
 * @name HuanxinDemo2
 * @class name：com.example.huanxindemo.util
 * @class describe
 * @time 2018/12/28 18:15
 * @change
 * @chang time
 * @class describe
 */
public class MessageUtil {
    public static String getMessageText(EMMessage msg) {
        if (msg.getType() == EMMessage.Type.TXT) {
            switch (msg.getType()) {
                case TXT:
                    return ((EMTextMessageBody) msg.getBody()).getMessage();
                case VOICE:
                    return "[语音]";
                case VIDEO:
                    return "[视频]";
                case IMAGE:
                    return "[图片]";
                case FILE:
                    return "[文件]";
                case LOCATION:
                    return "[位置]";
                case CMD:
                    return "[透传消息]";
                default:
                    break;
            }
        }
        return "";
    }
}
