package com.e.common.widget.image;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e.common.R;
import com.e.common.image.viewpager.ImagePagerActivity;
import com.e.common.utility.CommonUtility;
import com.e.common.widget.DialogExt;
import com.e.common.widget.FlowLayout;
import com.e.common.widget.effect.button.EffectImgButton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class ImageLayout.java Create on 2015-03-13 下午1:01
 * @description 处理多图上传
 */
public class ImageLayout extends LinearLayout implements View.OnClickListener {

    private Context mContext;

    FlowLayout mLayoutImgs;

    View mLayoutUploadPic;

    TextView mTextDesc;

    private LayoutInflater mInflater;
    // 存放当前图片的view
    private HashMap<View, String> mViews;
    DialogExt mDialog;

    private int maxPicNum = 1;

    public ImageLayout(Context context) {
        super(context);
        init();
    }

    public ImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        mContext = getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_imagelayout, this);

        mLayoutImgs = (FlowLayout) findViewById(R.id.ll_imgs);
        mLayoutUploadPic = findViewById(R.id.rl_upload_pic);
        mTextDesc = (TextView) findViewById(R.id.text_desc);
    }

    public void setMaxPicNum(int maxPicNum) {
        this.maxPicNum = maxPicNum;
    }

    public int getImageCount() {
        return mLayoutImgs.getChildCount();
    }


    public HashMap<View, String> getImageViewMap() {
        return mViews;
    }

    public void showPictures(String[] pictures) {
        if (CommonUtility.Utility.isNull(pictures) || pictures.length == 0) {
            return;
        }
        ArrayList<String> path = new ArrayList<>();
        for (String p : pictures) {
            path.add(p);
        }
        showPictures(path);
    }

    public void showPictures(ArrayList<String> paths) {
        if (CommonUtility.Utility.isNull(mInflater)) {
            mInflater = LayoutInflater.from(mContext);
            mViews = new HashMap<>();
        }
        for (String path : paths) {
            View view = mInflater.inflate(R.layout.item_img, null);
            ImageLoaderView imgShow = (ImageLoaderView) view
                    .findViewById(R.id.img_show);
            imgShow.setOnClickListener(this);
            EffectImgButton imgDelete = (EffectImgButton) view
                    .findViewById(R.id.effectBtn_delete_pic);
            String url = CommonUtility.ImageUtility.formatUrl(path);
            imgShow.loadImage(url);
            imgDelete.setTag(view);
            imgDelete.setOnClickListener(this);
            mLayoutImgs.addView(view, 0);
            mViews.put(view, path);
        }
        resetUploadBtnVisible();
        this.invalidate();
    }

    View.OnClickListener dialogOKClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            View imgDelete = (View) view.getTag();
            View imgView = (View) imgDelete.getTag();
            if (!CommonUtility.Utility.isNull(mViews)) {
                mViews.remove(imgView);
            }
            mLayoutImgs.removeView(imgView);
            resetUploadBtnVisible();
            mDialog.dismiss();
            mLayoutImgs.invalidate();
            if (mDeleteItemOnClickListener != null) {
                mDeleteItemOnClickListener.getDeleteItemOnClickListener();
            }

        }
    };

    /**
     * method desc：显示或者隐藏添加图片按钮
     */
    void resetUploadBtnVisible() {
        if (getImageCount() >= maxPicNum + 1) {
            mLayoutUploadPic.setVisibility(View.GONE);
        } else {
            mLayoutUploadPic.setVisibility(View.VISIBLE);
        }

        if (getImageCount() <= 1) {
            mTextDesc.setVisibility(View.VISIBLE);
        } else {
            mTextDesc.setVisibility(View.GONE);
        }
    }

    public void setTextDesc(String text) {
        mTextDesc.setText(text);
    }

    public void setTextDescColor(int color) {
        mTextDesc.setTextColor(color);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_show) {
            ImageLoaderView imageLoaderView = (ImageLoaderView) v;
            ImagePagerActivity.go2ImageViewPager(v.getContext(), mViews.values(), imageLoaderView.getCurrentPath());
        } else if (id == R.id.effectBtn_delete_pic) {
            mDialog = CommonUtility.DialogUtility.confirm(mContext, "确定删除图片么？");
            mDialog.setOnClickListener(dialogOKClickListener, v, null, null);
        }
    }

    public interface DeleteItemOnClickListener {
        void getDeleteItemOnClickListener();

    }

    private DeleteItemOnClickListener mDeleteItemOnClickListener;

    public void setDeleteItemOnClickListener(DeleteItemOnClickListener listener) {
        mDeleteItemOnClickListener = listener;
    }
}
