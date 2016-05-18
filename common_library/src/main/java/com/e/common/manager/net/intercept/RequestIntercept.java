package com.e.common.manager.net.intercept;

import com.e.common.manager.net.config.Params;

import java.util.HashMap;

/**
 * Created by Evan on 16/3/16.
 */
public interface RequestIntercept {

    void progress(Params params); //对请求前参数处理

    HashMap<String, String> encrypt(Params params); //数据加密
}
