package internet.com.larkmusic.network.netnew;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by sjning
 * created on: 2019-07-26 16:04
 * description:
 */
public class LrcInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            Request request = chain.request();
            // Header参数
//            [_candyGetLrcManager.requestSerializer setValue:@ forHTTPHeaderField:@];
//    [_candyGetLrcManager.requestSerializer setValue:@ forHTTPHeaderField:@];
//    [_candyGetLrcManager.requestSerializer setValue:@ forHTTPHeaderField:@];
//     [_candyGetLrcManager.requestSerializer setValue:@"*/*" forHTTPHeaderField:@"Accept"];
//     [_candyGetLrcManager.requestSerializer setValue:@ forHTTPHeaderField:@];
//
//       [_candyGetLrcManager.requestSerializer setValue:@ forHTTPHeaderField:@];
//
//      [_candyGetLrcManager.requestSerializer setValue:@ forHTTPHeaderField:@];
            Request.Builder builder = request.newBuilder();
            builder.addHeader("Accept", "*/*");
            builder.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
            builder.addHeader("Referer", "https://y.qq.com/portal/player.html");
            builder.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
            builder.addHeader("Host", "c.y.qq.com");
            builder.addHeader("Cookie", "pgv_pvid=8455821612; ts_uid=1596880404; pgv_pvi=9708980224; yq_index=0; pgv_si=s3191448576; pgv_info=ssid=s8059271672; ts_refer=ADTAGmyqq; yq_playdata=s; ts_last=y.qq.com/portal/player.html; yqq_stat=0; yq_playschange=0; player_exist=1; qqmusic_fromtag=66; yplayer_open=1");
            request = builder.build();

            Response response = chain.proceed(request);
            ResponseBody body = response.body();
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = Charset.defaultCharset();
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            String string = buffer.clone().readString(charset);
            string = string.substring(22);
            string = string.substring(0, string.length() - 1);
            Log.e("KK", string);
            ResponseBody responseBody = ResponseBody.create(contentType, string);
            response = response.newBuilder().body(responseBody).build();
            return response;
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}
