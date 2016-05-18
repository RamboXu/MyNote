package com.e.common.widget.viewpager.page;

/**
 * Created by Evan on 16/4/11.
 */
public class CommonBanner {
    public Object data;
    public String url;
    public OnPageClickListener onPageClickListener;

    public CommonBanner(Object data, String res) {
        this.data = data;
        this.url = res;
    }

    public CommonBanner(Object data, String url, OnPageClickListener listener) {
        this.data = data;
        this.url = url;
        this.onPageClickListener = listener;
    }
}