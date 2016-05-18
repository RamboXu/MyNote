package com.e.common.handler.callback;

import android.content.Context;

import com.e.common.i.IHandlerCallback;
import com.e.common.i.IJsonOperate;
import com.e.common.utility.CommonUtility;

/**
 * @{# HandlerCallback.java Create on 2014年11月25日 下午5:09:11
 * 
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description
 *
 */
public abstract class HandlerCallback implements IHandlerCallback {

	public abstract void onSuccess(Object o);

	@Override
	public void onFailure(Context context, Object o) {
		String result = ((IJsonOperate) context.getApplicationContext())
				.getRespMsg(o);
		if (!CommonUtility.Utility.isNull(result)) {
			CommonUtility.UIUtility.toast(context, result);
		}
	}

	@Override
	public void onRespNull() {

	}

}
