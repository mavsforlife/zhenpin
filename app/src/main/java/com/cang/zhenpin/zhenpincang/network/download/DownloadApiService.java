package com.cang.zhenpin.zhenpincang.network.download;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by victor on 2017/12/14.
 * Email: wwmdirk@gmail.com
 */

public interface DownloadApiService {

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}
