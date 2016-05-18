package com.e.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.e.common.R;
import com.e.common.utility.CommonUtility;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class InnerScrollEdittext.java Create on 2015-03-06 下午12:55
 * @description
 */
public class InnerScrollEditText extends LinearLayout {


    private Context mContext;

    private EditText mEditText;

    private InnerScrollView mInnerScrollView;

    public InnerScrollEditText(Context context) {
        super(context);
        init(null);
    }

    public InnerScrollEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mContext = getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_inner_scroll_edittext, this);

        mEditText = (EditText) findViewById(R.id.edit_text);
        mInnerScrollView = (InnerScrollView) findViewById(R.id.innerScrollView);
        if(!CommonUtility.Utility.isNull(attrs)) {
            TypedArray types = mContext.obtainStyledAttributes(attrs,
                    R.styleable.InnerScrollEditText, 0, 0);
            String textHint = types.getString(R.styleable.InnerScrollEditText_edittext_hint);
            mEditText.setHint(textHint);
        }
        CommonUtility.UIUtility.hideKeyboard(this);
    }

    public String getContent() {
        if(!CommonUtility.Utility.isNull(mEditText)) {
            return CommonUtility.UIUtility.getText(mEditText);
        }
        return null;
    }

    public void setContent(String content) {
        mEditText.setText(content);
        CommonUtility.UIUtility.setEditTextSection2End(mEditText);
    }

    public EditText getmEditText() {
        return mEditText;
    }

    /**
     * 设置背景色
     */
    @Override
    public void setBackgroundColor(int color) {
        mInnerScrollView.setBackgroundColor(color);
    }
}
