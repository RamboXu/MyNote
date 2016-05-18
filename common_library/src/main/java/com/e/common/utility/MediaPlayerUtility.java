package com.e.common.utility;

import android.media.MediaPlayer;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class MediaPlayerUtility.java Create on 2015-03-27 下午12:13
 * @description
 */
public class MediaPlayerUtility {

    private Object object;

    private MediaPlayer mMediaPlayer;

    private static MediaPlayerUtility mMediaPlayerUtility;

    public static MediaPlayerUtility getInstance() {
        if (CommonUtility.Utility.isNull(mMediaPlayerUtility)) {
            mMediaPlayerUtility = new MediaPlayerUtility();
        }
        return mMediaPlayerUtility;
    }

    public Object getTag() {
        return object;
    }

    public void setTag(Object object) {
        this.object = object;
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void play(String uri) {
        try {
            if (!CommonUtility.Utility.isNull(mMediaPlayer)) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            mMediaPlayer = new MediaPlayer();

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (!CommonUtility.Utility.isNull(mListener)) {
                        mListener.onVoiceFinished();
                        object = null;
                    }
                }
            });
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(uri);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (!CommonUtility.Utility.isNull(mMediaPlayer)) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private OnVoiceFinishedListener mListener;

    public void setOnVoiceFinishedListener(OnVoiceFinishedListener listener) {
        mListener = listener;
    }

    public interface OnVoiceFinishedListener {
        /**
         * 语音播放完毕后回掉
         */
        void onVoiceFinished();
    }
}
