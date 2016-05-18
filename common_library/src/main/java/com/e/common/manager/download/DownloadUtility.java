package com.e.common.manager.download;

import android.app.DownloadManager;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.e.common.utility.CommonUtility;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;

import java.io.File;
import java.io.Serializable;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class DownloadUtility.java Create on 2015-05-14 下午1:18
 * @description
 */
@EBean(scope = EBean.Scope.Singleton)
public class DownloadUtility implements Serializable {

    @SystemService
    DownloadManager mDownloadManager;

    public void download(String url, String destDirUrl) {
        if (!CommonUtility.Utility.isNull(url) && url.startsWith("http")) {
            String fileName = CommonUtility.FileUtility.getFileNameString(url);
            String path = new StringBuilder(destDirUrl).append(fileName).toString();
            if (!new File(path).exists()) {
                Uri resource = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(resource);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                request.setAllowedOverRoaming(false);
                //设置文件类型
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
                request.setMimeType(mimeString);
                //在通知栏中显示
                request.setShowRunningNotification(false);
                request.setVisibleInDownloadsUi(false);
                //sdcard的目录下的download文件夹
                request.setDestinationInExternalPublicDir(destDirUrl, fileName);
                try {
                    mDownloadManager.enqueue(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
