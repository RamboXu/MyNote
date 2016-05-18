package com.e.common.i;

import android.graphics.Bitmap;

/**
 * @{# IType.java Create on 2014年11月21日 下午1:31:57
 * 
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description
 *
 */
public interface IType {
	
	/**
	 * method desc： 将bitmap放入当前activity缓存中，生命周期与activity绑定
	 * 
	 * @param bitmap
	 */
	void addBitmap(Bitmap bitmap);

	/**
	 * method desc： 回收绑定当前activity的所有bitmap
	 */
	void destroyBitmaps();

}
