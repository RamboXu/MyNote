package com.e.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.e.common.R;
import com.e.common.utility.CommonUtility;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @{#} ItemView.java Create on 2014-10-9 下午10:04:16
 */

public class ItemLayoutEditText extends ItemLayout {

    /**
     * @param context
     */
    public ItemLayoutEditText(Context context) {
        super(context);
    }

    public ItemLayoutEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void inflateView() {
        CommonUtility.UIUtility.inflate(R.layout.layout_item_edit_view, this);
    }
}
