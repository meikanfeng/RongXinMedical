package com.huagu.RX.rongxinmedical.Interface;

/**
 * Created by fff on 2016/8/16.
 */
public interface RightClickCallBack {
    /**
     * 目前就用于listview gridview 适配器内部的点击回调
     * @param str   需传递的参数
     * @param i     这个是点击的item
     */
    void RightClick(String str,int i);
}
