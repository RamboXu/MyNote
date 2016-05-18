package com.e.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.e.common.R;
import com.e.common.constant.Constants;
import com.e.common.utility.CommonUtility;
import com.e.common.widget.CleanableEditText;

/**
 * @{# CommonInputActivity.java Create on 2014年12月8日 下午2:36:54
 *
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description
 *
 */
public class InputActivity extends BaseActivity {

	private CleanableEditText mEditContent;
	// 0 title, 1,当为数字类型时，限制输入数字的最大
	private String[] mObjects;
	private String mDefaultValue;
	private int mInputType = -1;
	private int mWhat;//用于区分是那个跳转过来的

	public static final int INPUT_TYPE_NUM = 1;
	public static final int INPUT_TYPE_WORD = -1;
	public static final int RESULT_CODE = 0x123;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_input);

		mEditContent = (CleanableEditText)findViewById(R.id.edit_content);
		mObjects = getIntent().getStringArrayExtra("mObjects");
		mDefaultValue = getIntent().getStringExtra("mDefaultValue");
		mInputType = getIntent().getIntExtra("mInputType",-1);
		mWhat = getIntent().getIntExtra("mWhat",0);
		initActionBar();
		mActionBar.setTextNavRight("确定");
		mActionBar.setNavTitle(mObjects[0].toString());

        mEditContent.setText(mDefaultValue);

        if (mInputType != -1) {
            mEditContent.setInputType(mInputType);
        }

		CommonUtility.UIUtility.showKeyboard(mEditContent);

		CommonUtility.UIUtility.setEditTextSection2End(mEditContent);
	}

	@Override
	public void onClick(View view) {

		super.onClick(view);
		if(view.getId() == R.id.ll_nav_right){
			String content = CommonUtility.UIUtility.getText(mEditContent);
			if (CommonUtility.Utility.isNull(content)) {
				CommonUtility.DialogUtility.tip(activity, "请输入内容");
				return;
			}
			Intent data = new Intent();
			data.putExtra(Constants.IDENTITY.IDENTITY_KEY, content);
			data.putExtra(Constants.IDENTITY.IDENTITY_INDEX,mWhat);
			setResult(Constants.IDENTITY.IDENTITY_RESULT_CODE_CONTENT_INPUT, data);
			finish();
		}

	}

	/**
	 * method desc：跳转到本界面
	 *
	 * @param activity
	 * @param inputType
	 *            -1为任意
	 * @param values
	 */
	public static void gotoInputAndReturn(Activity activity, TextView targetView, int inputType,
			String... values) {
        String defaultValue = CommonUtility.Utility.isNull(targetView) ? "" : CommonUtility.UIUtility.getText(targetView);
		Intent intent = new Intent(activity,InputActivity.class);

		intent.putExtra("mObjects",values);
		intent.putExtra("mDefaultValue",defaultValue);
		intent.putExtra("mInputType",inputType);
		intent.putExtra("mWhat",0);
		activity.startActivityForResult(intent, RESULT_CODE);
	}

	public static void gotoInputAndReturn(Activity activity, String defaultValue, int inputType,int what,
										  String... values) {
		Intent intent = new Intent(activity,InputActivity.class);
		intent.putExtra("mObjects",values);
		intent.putExtra("mDefaultValue",defaultValue);
		intent.putExtra("mInputType",inputType);
		intent.putExtra("mWhat",what);
		activity.startActivityForResult(intent, RESULT_CODE);
	}


	/**
	 * method desc：跳转到本界面
	 *
	 * @param activity
	 * @param values
	 */
	public static void gotoInputAndReturn(Activity activity, String... values) {
		gotoInputAndReturn(activity, "", 0, values);
	}

	public static void gotoInputAndReturn(Activity activity,String defaultValue,int mWhat, String... values) {
		gotoInputAndReturn(activity, defaultValue, -1, mWhat,values);
	}
}
