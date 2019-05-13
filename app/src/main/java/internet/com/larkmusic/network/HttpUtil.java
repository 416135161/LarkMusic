package internet.com.larkmusic.network;


import org.greenrobot.eventbus.EventBus;

import internet.com.larkmusic.action.ActionStopLoading;
import internet.com.larkmusic.bean.Song;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sjning
 * created on: 2019/1/9 下午3:01
 * description:
 */
public final class HttpUtil {

    private HttpUtil() {

    }



    public static StreamService getApiService(String host, Interceptor interceptor) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(host);
        if (interceptor != null) {
            builder.client(new OkHttpClient.Builder().addInterceptor(interceptor).build());
        }
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit client = builder.build();
        StreamService ss = client.create(StreamService.class);
        return ss;
    }

}
