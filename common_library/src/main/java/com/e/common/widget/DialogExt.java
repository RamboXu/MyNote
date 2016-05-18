package com.e.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e.common.R;
import com.e.common.utility.CommonUtility.Utility;

public class DialogExt extends Dialog implements OnClickListener {

    private Context context;
    private TextView title, message;
    View btn_divider;
    private TextView okBtn, cancelBtn;
    public final static int OK = 1;
    public final static int CANCEL = 2;

    private ViewGroup ll_content_view;
    public View customerView;

    public static DialogExt createDialog(Context context) {
        return new DialogExt(context, R.style.custome_dialog);
    }

    private DialogExt(Context context, int theme) {
        super(context, theme);
        this.context = context;
        setContentView(R.layout.layout_custome_dialog);
        title = (TextView) findViewById(R.id.id_text_title);
        message = (TextView) findViewById(R.id.id_text_message);
        okBtn = (TextView) findViewById(R.id.id_btn_ok);
        cancelBtn = (TextView) findViewById(R.id.id_btn_cancel);
        okBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        btn_divider = findViewById(R.id.id_btn_divider);
        ll_content_view = (ViewGroup) findViewById(R.id.ll_content_view);
        setCancelable(false);
    }

    public void setOnClickListener(View.OnClickListener okListener) {
        if (!Utility.isNull(okListener)) {
            okBtn.setOnClickListener(okListener);
        }
    }

    public void setOnClickListener(
            View.OnClickListener okListener,
            View.OnClickListener cancelListener) {
        if (!Utility.isNull(okListener)) {
            okBtn.setOnClickListener(okListener);
        }
        if (!Utility.isNull(cancelListener)) {
            cancelBtn.setOnClickListener(cancelListener);
        }
    }

    public void setOnClickListener(
            View.OnClickListener okListener, Object okTag,
            View.OnClickListener cancelListener, Object cancelTag) {
        if (!Utility.isNull(okListener)) {
            okBtn.setTag(okTag);
            okBtn.setOnClickListener(okListener);
        }
        if (!Utility.isNull(cancelListener)) {
            cancelBtn.setTag(cancelTag);
            cancelBtn.setOnClickListener(cancelListener);
        }
    }

    public void setTitle(String str) {
        title.setText(str);
        title.setVisibility(View.VISIBLE);
    }

    public void setTitle(int resStr) {
        title.setText(context.getString(resStr));
        title.setVisibility(View.VISIBLE);
    }

    public void setMessage(String str) {
        message.setText(str);
        title.setVisibility(View.VISIBLE);
    }

    public void setMessage(int resStr) {
        message.setText(context.getString(resStr));
        title.setVisibility(View.VISIBLE);
    }

    public TextView getMessageView() {
        return message;
    }

    public void setView(View view) {
        ll_content_view.removeAllViews();
        ll_content_view.addView(view);
        this.customerView = view;
    }

    public TextView getCancelBtn() {
        return cancelBtn;
    }

    public TextView getOKBtn() {
        return okBtn;
    }

    /**
     * 设置显示单个按钮
     *
     * @param btnType 显示按钮的类型
     */
    public void setSingleBtn(int btnType) {
        if (btnType == OK) {
            cancelBtn.setVisibility(View.GONE);
            okBtn.setBackgroundResource(R.drawable.xml_btn_dialog_single);
        } else if (btnType == CANCEL) {
            okBtn.setVisibility(View.GONE);
            cancelBtn.setBackgroundResource(R.drawable.xml_btn_dialog_single);
        }
        btn_divider.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
