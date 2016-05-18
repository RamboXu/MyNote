package com.e.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.e.common.R;
import com.e.common.utility.CommonUtility;
import com.e.common.utility.RecordingUtility;
import com.e.common.utility.RecordingUtility_;
import com.e.common.widget.image.ImageLoaderView;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class RecordDialog.java Create on 2015-03-23 下午3:20
 * @description
 */
public class RecordDialog extends Dialog{

    private ImageLoaderView mImageRecordingAnim;
    private View mLayoutRecording;
    private View mLayoutCancelTip, mLayoutTimeTooShort;

    private RecordingUtility mRecordingUtility;
    
    private int mVoiceValue;

    public RecordDialog(Context context) {
        super(context);
        init();
    }

    public RecordDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_recording);
        mImageRecordingAnim = (ImageLoaderView) findViewById(R.id.img_recording_anim);
        mLayoutCancelTip = findViewById(R.id.ll_cancel_tip);
        mLayoutRecording = findViewById(R.id.ll_recording);
        mLayoutTimeTooShort = findViewById(R.id.ll_time_too_short);
        mRecordingUtility = RecordingUtility_.getInstance_(getContext());

    }

    public void recordingState() {
        mLayoutCancelTip.setVisibility(View.GONE);
        mLayoutRecording.setVisibility(View.VISIBLE);
    }

    public void startRecording() {
        recordingState();
        mVoiceValue = 0;
        mRecordingUtility.startRecord();
        new Thread(ImgThread).start();
    }

    public void stopRecording() {
        stopRecordingState();
        mRecordingUtility.stopRecord();
    }

    public void stopRecordingState() {
        mLayoutCancelTip.setVisibility(View.VISIBLE);
        mLayoutRecording.setVisibility(View.GONE);
    }

    public void showTimeTooShort() {
        mLayoutTimeTooShort.setVisibility(View.VISIBLE);
        mLayoutCancelTip.setVisibility(View.GONE);
        mLayoutRecording.setVisibility(View.GONE);

        imgHandle.sendEmptyMessage(2);
    }

    public boolean isRecordingState() {
        return mLayoutRecording.isShown();
    }

    public int getVoiceLength() {
        return mRecordingUtility.getVoiceTime();
    }

    public String getVoicePath() {
        return mRecordingUtility.getVoicePath();
    }

    public void deleteRecordFile() {
        mRecordingUtility.deleteRecordFile();
    }

    // 录音线程
    private Runnable ImgThread = new Runnable() {

        @Override
        public void run() {
            while (!CommonUtility.Utility.isNull(mRecordingUtility)
                    && mRecordingUtility.isRecording()) {
                try {
                    Thread.sleep(200);
                    mVoiceValue = mRecordingUtility.getVolume();
                    imgHandle.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Handler imgHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 2:
                    try {
                        Thread.sleep(1000);
                        mLayoutTimeTooShort.setVisibility(View.GONE);
                        mRecordingUtility.deleteRecordFile();
                        mRecordingUtility = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dismiss();
                    break;
                case 1:
                    setDialogImage();
                    break;
            }

        }
    };

    // 录音Dialog图片随声音大小切换
    void setDialogImage() {
        if (mVoiceValue < 200.0) {
            mImageRecordingAnim.loadLocalDrawable(R.drawable.record_volume_1);
        } else if (mVoiceValue > 200.0 && mVoiceValue < 400) {
            mImageRecordingAnim.loadLocalDrawable(R.drawable.record_volume_2);
        } else if (mVoiceValue > 400.0 && mVoiceValue < 800) {
            mImageRecordingAnim.loadLocalDrawable(R.drawable.record_volume_3);
        } else if (mVoiceValue > 800.0 && mVoiceValue < 1600) {
            mImageRecordingAnim.loadLocalDrawable(R.drawable.record_volume_4);
        } else if (mVoiceValue > 1600.0 && mVoiceValue < 3200) {
            mImageRecordingAnim.loadLocalDrawable(R.drawable.record_volume_5);
        } else if (mVoiceValue > 3200.0 && mVoiceValue < 5000) {
            mImageRecordingAnim.loadLocalDrawable(R.drawable.record_volume_6);
        } else if (mVoiceValue > 5000.0 && mVoiceValue < 7000) {
            mImageRecordingAnim.loadLocalDrawable(R.drawable.record_volume_7);
        } else if (mVoiceValue > 7000.0 && mVoiceValue < 10000.0) {
            mImageRecordingAnim.loadLocalDrawable(R.drawable.record_volume_8);
        } else if (mVoiceValue > 10000.0 && mVoiceValue < 14000.0) {
            mImageRecordingAnim.loadLocalDrawable(R.drawable.record_volume_9);
        } else if (mVoiceValue > 14000.0 && mVoiceValue < 17000.0) {
            mImageRecordingAnim.loadLocalDrawable(R.drawable.record_volume_10);
        } else if (mVoiceValue > 17000.0 && mVoiceValue < 20000.0) {
            mImageRecordingAnim.loadLocalDrawable(R.drawable.record_volume_11);
        } else if (mVoiceValue > 20000.0) {
            mImageRecordingAnim.loadLocalDrawable(R.drawable.record_volume_11);
        }
    }

    /**
     * 判断当前是否有效
     * 可能会因为权限的问题导致录音失败，需要重新实例化
     * @return
     */
    public boolean isValid() {
        return mRecordingUtility.isValid();
    }
}
