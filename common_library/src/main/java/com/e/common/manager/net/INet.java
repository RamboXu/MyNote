package com.e.common.manager.net;

import com.e.common.i.IJsonOperate;

import java.util.HashMap;

/**
 * @{# INet.java Create on 2014年11月20日 下午2:37:40
 * 
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description
 *
 */
public interface INet extends IJsonOperate {

	/**
	 * method desc： 获取网络请求地址
	 * 
	 * @return
	 */
	String getBaseRequestPath();


	/**
	 * method desc：获取header信息
	 *
	 * @return
	 */
	HashMap<String, String> getHeaderInfo();
}
