package com.e.common.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.e.common.R;
import com.e.common.event.EventTypeRequest;
import com.e.common.handler.callback.HandlerCallback;
import com.e.common.manager.net.INet;
import com.e.common.utility.CommonUtility;
import com.e.common.widget.LoadingView;

public class RequestHandler extends Handler {

    private HandlerCallback mCallBack;
    private Context context;

    public RequestHandler(Context context, HandlerCallback mCallback) {
        this.context = context;
        this.mCallBack = mCallback;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        LoadingView.hide(context);
        if (!CommonUtility.Utility.isNull(mCallBack)) {
            EventTypeRequest event = (EventTypeRequest) msg.obj;
            if (event instanceof EventTypeRequest) {
                int resultCode = event.getResultCode();
                if (resultCode == EventTypeRequest.RESULT_CODE_EXCEPTION_SERVER) {
                    if (event.isNeedShowErrorTip()) {
                        CommonUtility.UIUtility.toast(context, R.string.net_tip_server_wrong);
                    }
                    mCallBack.onFailure(context, event.getData());
                } else if (resultCode == EventTypeRequest.RESULT_CODE_EXCEPTION_DEVICE) {
                    if (event.isNeedShowErrorTip()) {
                        CommonUtility.UIUtility.toast(context, R.string.net_tip_no_net);
                    }
                    mCallBack.onFailure(context, event.getData());
                } else if (resultCode == EventTypeRequest.RESULT_CODE_EXCEPTION_NET_NOT_GOOD) {
                    if (event.isNeedShowErrorTip()) {
                        CommonUtility.UIUtility.toast(context, R.string.net_tip_not_good);
                    }
                    mCallBack.onFailure(context, event.getData());
                } else {
                    if (context.getApplicationContext() instanceof INet) {
                        if (event.isOtherRedirectUrl() || ((INet) context.getApplicationContext()).isRespSuccess(event.getData())) {
                            mCallBack.onSuccess(event.getData());
                        } else {
                            mCallBack.onFailure(context, event.getData());
                        }
                    } else {
                        mCallBack.onFailure(context, event.getData());
                    }
                }
            }
        }
        mCallBack = null;
    }

    public Context getContext() {
        return context;
    }
}
