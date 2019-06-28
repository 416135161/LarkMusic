package internet.com.larkmusic.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import internet.com.larkmusic.app.MusicApplication;
import internet.com.larkmusic.bean.Album;
import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2018/6/20 下午3:49
 * description: 存储入驻相关的数据
 */

public final class SpHelper {

    //循环模式
    public static final String KEY_RECYCLE_MODE = "recycle_mode";
    //喜欢的歌曲
    public static final String KEY_FAVORITE_SONGS = "key_favorite_songs";
    //喜欢的专辑列表
    public static final String KEY_FAVORITE_ALBUMS = "key_favorite_albums";
    //最近播放的歌曲
    public static final String KEY_RECENT_SONGS = "key_recent_songs";
    //搜索历史列表
    public static final String KEY_SEARCH_HISTORY = "key_search_history";


    public static List<Song> favoriteSongs;

    public static List<Song> recentSongs;

    public static List<Album> favoriteAlbums;

    public static List<String> searchHistory;

    static {
        Gson gs = new Gson();
        favoriteSongs = gs.fromJson(getDefault().getString(KEY_FAVORITE_SONGS, "[]"), new TypeToken<List<Song>>() {
        }.getType());

        recentSongs = gs.fromJson(getDefault().getString(KEY_RECENT_SONGS, "[]"), new TypeToken<List<Song>>() {
        }.getType());

        favoriteAlbums = gs.fromJson(getDefault().getString(KEY_FAVORITE_ALBUMS, "[]"), new TypeToken<List<Album>>() {
        }.getType());

        searchHistory = gs.fromJson(getDefault().getString(KEY_SEARCH_HISTORY, "[]"), new TypeToken<List<String>>() {
        }.getType());

    }

    private String spName = "myData";
    private static SpHelper mInstance;
    private Context mContext;

    private SpHelper() {
        mContext = MusicApplication.getInstance();
    }

    public static synchronized SpHelper getDefault() {
        if (mInstance == null) {
            mInstance = new SpHelper();
        }
        return mInstance;
    }

    public SpHelper removeKey(String key) {
        getSharedPreferencesEditor().remove(key).commit();
        return this;
    }

    public SpHelper putString(String key, String value) {
        getSharedPreferencesEditor().putString(key, value).commit();
        return this;
    }

    public SpHelper putInt(String key, int value) {
        getSharedPreferencesEditor().putInt(key, value).commit();
        return this;
    }

    public SpHelper putBoolean(String key, boolean value) {
        getSharedPreferencesEditor().putBoolean(key, value).commit();
        return this;
    }

    public SpHelper putLong(String key, long value) {
        getSharedPreferencesEditor().putLong(key, value).commit();
        return this;
    }

    public String getString(String key, String defValue) {
        return getSharedPreferences().getString(key, defValue);
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public int getInt(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public long getLong(String key, long defValue) {
        return getSharedPreferences().getLong(key, defValue);
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    private SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getSharedPreferencesEditor() {
        return mContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
                .edit();
    }

}
