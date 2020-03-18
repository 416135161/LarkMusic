package internet.com.larkmusic.network.netnew;


import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;


import internet.com.larkmusic.BuildConfig;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.http.Field;

/**
 * Created by sjning
 * created on: 2019-07-26 11:34
 * description:
 */
public class EncryptInterceptor implements Interceptor {
    private Request request;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //这个是请求的url，也就是咱们前面配置的baseUrl
        String url = request.url().toString();
        //这个是请求方法
        String method = request.method();
        long t1 = System.nanoTime();
        request = encrypt(request);//模拟的加密方法
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
//        response = decrypt(response);
        return response;
    }

    //加密
    private Request encrypt(Request request) throws IOException {
        //获取请求body，只有@Body 参数的requestBody 才不会为 null
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            okio.Buffer buffer = new okio.Buffer();
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }

            String string = buffer.readString(charset);
            //模拟加密的方法，这里调用大家自己的加密方法就可以了
            String encryptStr = encrypt(string);
            if (BuildConfig.DEBUG) {
                Log.e("JJJJ", decrypt(encryptStr));
            }
//            RequestBody body = MultipartBody.create(contentType, encryptStr);

            FormBody.Builder formBodyBuild = new FormBody.Builder();
            formBodyBuild.add("pdatas", encryptStr);
            request = request.newBuilder()
                    .post(formBodyBuild.build())
                    .build();

        }
        return request;
    }

    private Response decrypt(Response response) throws IOException {
        if (response.isSuccessful()) {
            //the response data
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
            //解密方法，需要自己去实现
            String bodyString = decrypt(string);
            ResponseBody responseBody = ResponseBody.create(contentType, bodyString);
            response = response.newBuilder().body(responseBody).build();
        }
        return response;
    }

    //模拟加密的方法
    private String encrypt(String string) {
        try {
            if (BuildConfig.DEBUG) {
                Log.e("encrypt", string);
            }
            return AES.Encrypt(string, AES.KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //模拟解密的方法
    private String decrypt(String string) {
        try {
            if (BuildConfig.DEBUG) {
                Log.e("decrypt", string);
            }
            return AES.Decrypt(string, AES.KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
}