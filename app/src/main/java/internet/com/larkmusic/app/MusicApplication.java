package internet.com.larkmusic.app;

import android.app.Application;
import android.graphics.Typeface;

import internet.com.larkmusic.network.Config;

/**
 * Created by sjning
 * created on: 2019/5/13 下午4:25
 * description:
 */
public class MusicApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Config.tf4 = Typeface.createFromAsset(getAssets(), "fonts/Intro_Cond_Light.otf");
            Config.tf3 = Typeface.createFromAsset(getAssets(), "fonts/Gidole-Regular.ttf");
            Config.tfLark = Typeface.createFromAsset(getAssets(), "fonts/Century_Gothic_W02.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
