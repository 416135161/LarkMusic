package internet.com.larkmusic.network;

import android.graphics.Typeface;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by sjning
 * created on: 2018/10/25 上午10:21
 * description:
 */
public final class Config {

    private Config() {
    }

    final public static int COUNT = 5;
    public static int PLAY_ADS_COUNT = 0;
    public static int MAX_PLAY_COUNT = 100;
    public static int SEARCH_COUNT = 40;

    public static Typeface tf3;
    public static Typeface tf4;
    public static Typeface tfLark;

    public static final String GENIUS = "genius";
    public static String HOST_GET_SONG = "http://wwwapi.kugou.com";
    public static String HOST_GET_PLAY_URL = "http://39.107.89.143";
    public static String HOST_GET_LRC = "http://lyrics.kugou.com";
    public static String HOST_GET_IMAGE = "http://kmr.service.kugou.com";
    //测试
//    public static final String API_HOST = "http://101.200.200.156:8080";
    //生产
    public static final String API_HOST = "http://39.98.242.57:8080";

    public static String FROM;
    public static String FROM_US = "0";
    public static String FROM_JAPAN = "1";
    public static String FROM_CHINESE = "2";

    /**
     * 如果当前语言是日语，则URL不加参数
     */
    static {
        Locale locale = Locale.getDefault();
        // 获取当前系统语言
        String lang = locale.getLanguage();
        if (TextUtils.equals(lang, "ja")) {
            FROM = FROM_JAPAN;
        } else {
            FROM = FROM_US;
        }
    }

    public static String getFrom() {
        Locale locale = Locale.getDefault();
        // 获取当前系统语言
        String lang = locale.getLanguage();
        if (TextUtils.equals(lang, "ja")) {
            return FROM_JAPAN;
        } else if (TextUtils.equals(lang, "zh")) {
            return FROM_CHINESE;
        } else {
            return FROM_US;
        }
    }

}
