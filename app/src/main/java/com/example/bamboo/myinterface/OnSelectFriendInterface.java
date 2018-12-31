package com.example.bamboo.myinterface;

import java.util.List;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.myinterface
 * @class 查询好友列表接口
 * @time 2018/12/31 17:33
 * @change
 * @chang time
 * @class describe
 */
public interface OnSelectFriendInterface {
    /**
     * 查询好友列表接口回调
     *
     * @param friendList 好友列表
     * @param error      错误码
     */
    void onSelect(List<String> friendList, int error);
}
