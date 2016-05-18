package com.e.common.image.viewpager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

import com.e.common.R;
import com.e.common.activity.BaseActivity;

import java.util.ArrayList;
import java.util.Collection;

public class ImagePagerActivity extends BaseActivity {
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_pager);

        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        ArrayList<String> urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);

        mPager = (HackyViewPager) findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(
                getSupportFragmentManager(), urls);

        mPager.setAdapter(mAdapter);
        indicator = (TextView) findViewById(R.id.indicator);

        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
                .getAdapter().getCount());
        indicator.setText(text);
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator,
                        arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<String> fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position);
            return ImageDetailFragment.newInstance(url);
        }
    }

    /**
     * @param context
     * @param paths    需要显示的图片的url集合
     * @param showPath 定位显示图片的地址
     */
    public static void go2ImageViewPager(Context context, ArrayList<String> paths, String showPath) {
        int position = 0;
        for (int i = 0; i < paths.size(); i++) {
            if (paths.get(i).toString().contains(showPath)) {
                position = i;
                break;
            }
        }
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, paths);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }

    public static void go2ImageViewPager(Context context, String[] paths, String showPath) {
        ArrayList<String> pathList = new ArrayList<>();
        for (int i = 0; i < paths.length; i++) {
            pathList.add(paths[i]);
        }
        go2ImageViewPager(context, pathList, showPath);
    }

    public static void go2ImageViewPager(Context context, Collection paths, String showPath) {
        ArrayList<String> pathList = new ArrayList<>();
        for (Object path : paths) {
            if (path instanceof String) {
                pathList.add(path.toString());
            }
        }
        go2ImageViewPager(context, pathList, showPath);
    }
}