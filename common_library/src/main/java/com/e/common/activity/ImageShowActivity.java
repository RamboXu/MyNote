/**
 * @{#} ImageShowActivity.java Create on 2013-8-21 下午4:12:37
 * @author Evan
 * @email evan0502@qq.com
 * @version 1.0
 */
package com.e.common.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;

import com.e.common.R;
import com.e.common.constant.Constants.IDENTITY;
import com.e.common.widget.image.PinchImageView;

@SuppressLint("HandlerLeak")
public class ImageShowActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        try {
            String path = getIntent()
                    .getStringExtra(IDENTITY.IDENTITY_FILEPATH).toString();
            PinchImageView imageView = (PinchImageView) findViewById(R.id.image_big_show);
            imageView.loadImage(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
