package com.e.common.widget.effect.button;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.e.common.utility.CommonUtility;

/**
 * Created by Evan on 16/3/26.
 */
public class CheckLayout extends LinearLayout implements View.OnClickListener {

    public CheckLayout(Context context) {
        super(context);
    }

    public CheckLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(View v) {
        EffectColorButton btn = (EffectColorButton) v;
        if (btn.isChecked()) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            EffectColorButton checkView = (EffectColorButton) getChildAt(i);
            checkView.setChecked(false);
        }
        ((EffectColorButton) v).setChecked(true);
        if (!CommonUtility.Utility.isNull(mListener)) {
            mListener.onChecked(v);
        }
    }

    public interface OnCheckedListener {
        void onChecked(View view);
    }

    OnCheckedListener mListener;

    public void setOnCheckedListener(OnCheckedListener listener) {
        this.mListener = listener;

        for (int i = 0; i < getChildCount(); i++) {
            EffectColorButton checkView = (EffectColorButton) getChildAt(i);
            checkView.setOnClickListener(this);
        }
    }

    /**
     * 获取当前被选中的view
     * @return
     */
    public View getCurrentCheckedView() {
        for (int i = 0; i < getChildCount(); i++) {
            EffectColorButton checkView = (EffectColorButton) getChildAt(i);
            if(checkView.isChecked()) {
                return checkView;
            }
        }
        return null;
    }
}
