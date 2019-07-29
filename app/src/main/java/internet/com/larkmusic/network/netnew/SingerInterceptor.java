package internet.com.larkmusic.network.netnew;

import android.os.Build;
import android.text.TextUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sjning
 * created on: 2019-07-29 11:23
 * description:
 */
public class SingerInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            Request request = chain.request();
            // Header参数
            Request.Builder builder = request.newBuilder();
            builder.addHeader("Referer", "http://y.qq.com");
            request = builder.build();
            return chain.proceed(request);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}
