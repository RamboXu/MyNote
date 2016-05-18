/**
 * @{#} InitManager.java Create on 2014年11月13日 下午2:52:53
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description
 */
package com.e.common.manager.init;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.e.common.manager.image.OldRoundedBitmapDisplayer;
import com.e.common.manager.net.NetManager;
import com.e.common.utility.CommonUtility;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean(scope = EBean.Scope.Singleton)
public class InitManager {

    @Bean
    NetManager mNetManager;

    @RootContext
    Context mContext;

    private final String IDENTITY_FIRST_INIT_IMAGE_LOADER_1 = "IDENTITY_FIRST_INIT_IMAGE_LOADER_1";

    /**
     * 初始化ImageLoader配置
     *
     * @param context
     */
    public void initImageLoaderConfig(Context context) {
        DisplayImageOptions options = getOptions(0, false);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .defaultDisplayImageOptions(options)
                .memoryCacheSize(10 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .memoryCache(new WeakMemoryCache())
                .imageDownloader(
                        new BaseImageDownloader(context, 30 * 1000, 30 * 1000))
                        // connectTimeout
                        // (5
                        // s),
                        // readTimeout
                        // (30
                        // s)超时时间
                .diskCacheFileCount(100)// 缓存一百张图片
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);

        boolean isFirstInit = CommonUtility.SharedPreferencesUtility.getBoolean(mContext, IDENTITY_FIRST_INIT_IMAGE_LOADER_1, true);
        if (isFirstInit) {
            ImageLoader.getInstance().clearDiskCache();
            CommonUtility.SharedPreferencesUtility.put(mContext, IDENTITY_FIRST_INIT_IMAGE_LOADER_1, false);
        }
    }

    public DisplayImageOptions getOptions(int roundSize, boolean isFadeIn) {
        return getOptionBuilder(roundSize, isFadeIn).build();
    }

    public DisplayImageOptions.Builder getOptionBuilder(int roundSize, boolean isFadeIn) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .cacheInMemory(false).cacheOnDisk(true)
                .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565);
        if (roundSize > 0) {
            builder.displayer(new OldRoundedBitmapDisplayer(roundSize, isFadeIn));
        } else if (isFadeIn) {
            builder.displayer(new FadeInBitmapDisplayer(1000));
        }
        return builder;
    }

    /**
     * 给imageloader 显示初始的图片，不需要有质量上的压缩
     *
     * @return
     */
    public DisplayImageOptions.Builder getOptionsForInitLoad(int roundSize) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .cacheInMemory(false).cacheOnDisk(false)
                .considerExifParams(true).bitmapConfig(Bitmap.Config.ARGB_8888);
        if (roundSize > 0) {
            builder.displayer(new OldRoundedBitmapDisplayer(roundSize, false));
        }
        return builder;
    }
}