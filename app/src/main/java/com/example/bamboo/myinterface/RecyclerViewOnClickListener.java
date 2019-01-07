package com.example.bamboo.myinterface;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.myinterface
 * @class recyclerView的点击事件
 * @time 2018/12/4 16:47
 * @change
 * @chang time
 * @class describe
 */
public interface RecyclerViewOnClickListener {
    /**
     * 点击事件
     *
     * @param obj      Object对象
     * @param position 点击的列表索引
     */
    void onClick(Object obj, int position, Object tag);

}
