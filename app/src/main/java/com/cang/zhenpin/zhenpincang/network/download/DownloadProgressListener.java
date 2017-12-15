package com.cang.zhenpin.zhenpincang.network.download;

/**
 * Created by victor on 2017/12/14.
 * Email: wwmdirk@gmail.com
 */

public interface DownloadProgressListener {

    void update(long bytesRead, long contentLength, boolean done);
}
