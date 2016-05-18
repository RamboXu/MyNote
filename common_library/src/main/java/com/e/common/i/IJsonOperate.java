package com.e.common.i;


/**    
 * @{#} IJsonOperate.java Create on 2014年11月30日 下午1:11:55    
 *    
 * @author <a href="mailto:evan0502@qq.com">Evan</a>   
 * @version 1.0    
 * @description
 * 必须由application来实现该接口
 *	
 */
public interface IJsonOperate {

	/**
	 * method desc：判断请求是否成功
	 *
	 * @param object
	 */
	boolean isRespSuccess(Object object);
	
	/**
	 * method desc：获取服务端返回提示信息
	 *
	 * @param object
	 * @return
	 */
	String getRespMsg(Object object);

	String getRespCode(Object object);
}
