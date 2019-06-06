package internet.com.larkmusic.util;

import android.content.Context;
import android.content.SharedPreferences;

import internet.com.larkmusic.app.MusicApplication;

/**
 * Created by sjning
 * created on: 2018/6/20 下午3:49
 * description: 存储入驻相关的数据
 */

public final class SpHelper {


    public static final String KEY_RECYCLE_MODE = "recycle_mode";

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
