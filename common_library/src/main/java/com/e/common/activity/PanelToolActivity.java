package com.e.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.e.common.R;
import com.e.common.constant.Constants;
import com.e.common.utility.CommonUtility;
import com.e.common.widget.effect.button.EffectColorButton;

/**
 * 防ios拍照底部弹出的panel，可以自己定义显示数量
 */
public class PanelToolActivity extends BaseActivity {

    public static final String EXTRA_PANEL_ARRAY_KEY = "panel_array_key";
    public static final String EXTRA_PANEL_ARRAY_KEY_COLOR = "panel_array_key_color";
    private LinearLayout mPanelContainerView;

    public static void startForResult(Activity activity, String[] text, int requestCode) {
        startForResult(activity, text, null, requestCode);
    }

    public static void startForResult(Fragment fragment, String[] text, int requestCode) {
        startForResult(fragment, text, null, requestCode);
    }


    public static void startForResult(Activity activity, String[] text, int[] color, int requestCode) {
        Intent panelIntent = new Intent(activity, PanelToolActivity.class);

        panelIntent.putExtra(EXTRA_PANEL_ARRAY_KEY, text);
        if (!CommonUtility.Utility.isNull(color)) {
            panelIntent.putExtra(EXTRA_PANEL_ARRAY_KEY_COLOR, color);
        }
        activity.startActivityForResult(panelIntent, requestCode);
    }

    public static void startForResult(Fragment fragment, String[] text, int[] color, int requestCode) {
        Intent panelIntent = new Intent(fragment.getActivity(), PanelToolActivity.class);

        panelIntent.putExtra(EXTRA_PANEL_ARRAY_KEY, text);
        if (!CommonUtility.Utility.isNull(color)) {
            panelIntent.putExtra(EXTRA_PANEL_ARRAY_KEY_COLOR, color);
        }
        fragment.startActivityForResult(panelIntent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_panel_tool);
        getWindow().setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        String[] strArray = getIntent().getStringArrayExtra(EXTRA_PANEL_ARRAY_KEY);
        if (strArray == null || strArray.length == 0) {
            CommonUtility.DebugLog.e("DataSource(String Array) is empty!!!");
            PanelToolActivity.this.finish();
        }

        int[] color = getIntent().getIntArrayExtra(EXTRA_PANEL_ARRAY_KEY_COLOR);

        mPanelContainerView = (LinearLayout) findViewById(R.id.panel_container_view);
        LayoutInflater inflater = LayoutInflater.from(this);
        int length = strArray.length;
        for (int i = 0; i < length; i++) {
            String str = strArray[i];
            EffectColorButton btn = (EffectColorButton) inflater.inflate(R.layout.view_panel_button_item, null);
            btn.setHeight(getResources().getDimensionPixelSize(R.dimen.size_dp_50));
            btn.setId(i);
            btn.setText(str);
            if (!CommonUtility.Utility.isNull(color)) {
                btn.setTextColor(color[i]);
            }
            btn.setOnClickListener(this);
            mPanelContainerView.addView(btn);
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id != R.id.btn_cancel) {
            Intent intent = new Intent();
            intent.putExtra(Constants.IDENTITY.IDENTITY_KEY, id);
            setResult(Constants.IDENTITY.ACTIVITY_CHOOSEFILE_CODE, intent);
        }
        finish();
    }
}
