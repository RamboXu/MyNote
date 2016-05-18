/**
 * @{#} RecordingUtil.java Create on 2014年7月15日 下午9:02:17
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 */
package com.e.common.utility;

import android.content.Context;
import android.media.MediaRecorder;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.File;
import java.io.IOException;

@EBean
public class RecordingUtility {

    @RootContext
    Context mContext;

    private File mRecordFile;

    private MediaRecorder mr;
    private boolean isRun;

    private String mVoicePath;
    private int mTime;

    private boolean isValid = true;


    private void startRecording() {
        if (!CommonUtility.Utility.isNull(mVoicePath)) {
            mRecordFile = new File(mVoicePath);

            if (mRecordFile.exists()) {
                mRecordFile.delete();
            }
            try {
                mRecordFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                mr = new MediaRecorder();
                mr.setAudioSource(MediaRecorder.AudioSource.MIC);
                mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mr.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                // recorder.setAudioChannels(AudioFormat.CHANNEL_CONFIGURATION_MONO);
                mr.setAudioSamplingRate(8000);
                mr.setOutputFile(mVoicePath);
                mr.prepare();
                mr.start();
                isRun = true;
                new Thread() {
                    public void run() {
                        while (isRun) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mTime++;
                        }
                    }
                }.start();
            } catch (Exception e) {
                e.printStackTrace();
                isValid = false;
                stopRecord();
                mRecordFile.delete();
            }
        } else {
            CommonUtility.UIUtility.toast(mContext, "初始化文件失败，请重新登录后再试");
        }
    }

    public void startRecord() {
        mVoicePath = CommonUtility.FileUtility.getUUIDVoicePath();
        startRecording();
    }

    public void stopRecord() {
        try {
            if (mr == null) {
                return;
            }
            isRun = false;
            mr.stop();
            mr.reset();
            mr.release();
            mr = null;
        } catch (Exception e) {
            e.printStackTrace();
            isValid = false;
        }
    }

    public void deleteRecordFile() {
        if (!CommonUtility.Utility.isNull(mRecordFile) && mRecordFile.isFile()) {
            mRecordFile.delete();
        }
    }

    public String getVoicePath() {
        return this.mVoicePath;
    }

    public int getVoiceTime() {
        return mTime;
    }

    public boolean isRecording() {
        return isRun;
    }

    public boolean isValid() {
        return isValid;
    }

    public int getVolume() {
        try {
            if (!CommonUtility.Utility.isNull(mr)) {
                return (mr.getMaxAmplitude());
            } else
                return 0;
        } catch (Exception e) {
        }
        return 0;
    }
}
