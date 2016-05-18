package com.e.common.i;

import android.content.Context;


public interface IHandlerCallback {

	void onFailure(Context context, Object o);
	
	void onRespNull();
}
