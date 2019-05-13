package internet.com.larkmusic.network;

import android.os.Build;
import android.text.TextUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sjning
 * created on: 2019/1/8 下午3:16
 * description:
 */
public class QueryInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            Request request = chain.request();
            // Header参数
            Request.Builder builder = request.newBuilder();
//            builder.addHeader("Accept-Language", "en-CN;q=1, zh-Hans-CN;q=0.9");
//            builder.addHeader("User-agent", "KugouMusic/2.6.6 (Mac OS X Version 10.13.6 (Build 17G65))");
//            builder.addHeader("Cookie", "kg_mid=d02c7c08865b3a81ccda44b4c6dba725");
//            builder.addHeader("Host", "songsearch.kugou.com");
            request = builder.build();
            return chain.proceed(request);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}
