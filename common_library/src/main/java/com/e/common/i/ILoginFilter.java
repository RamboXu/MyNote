package com.e.common.i;

/**
 * @{# ILoginFilter.java Create on 2014年11月25日 下午2:47:20
 * 
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description
 *
 */
public interface ILoginFilter {

	/**
	 * method desc：判断是否需要登录后才能操作
	 *
	 * @param object
     * @param resId
	 */
	boolean doFilterLogin(Object object, int resId);

	/**
	 * method desc：需要登录后操作的标识，在进入activity或者fragment时进行添加 e.g 通常为view.getId() see
	 * {@link #doFilterLogin(Object object, int view)}
	 *
	 * @param object
     * @param resId
	 */
	void putFilterKey(Object object, int resId);

}
