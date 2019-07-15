package internet.com.larkmusic.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import internet.com.larkmusic.activity.MainActivity;
import internet.com.larkmusic.app.MusicApplication;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.network.Config;

/**
 * Created by sjning
 * created on: 2019/1/12 下午9:23
 * description:
 */
public final class CommonUtil {


    public static void setTvBoldFace(TextView textView) {
        textView.setTypeface(Config.tfLark);
        textView.getPaint().setFakeBoldText(true);
    }

    public static Intent getNowPlayingIntent(Context context) {

        final Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Constants.NAVIGATE_NOW_PLAYING);
        return intent;
    }

    public static String getMetaData(@NonNull Context context, @NonNull String key) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getApplicationContext()
                    .getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null && appInfo.metaData != null) {
                String serverName = appInfo.metaData.getString(key);
                return serverName;
            } else {
                Log.e("music lark", "需要在AndroidManifest.xml中配置WebviewUrl meta数据");
                return "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getLocalPath(String hash) {
        List<File> fileList = FileUtils.listFilesInDir(getLocalSavePath());
        if (fileList != null && fileList.size() > 0) {
            for (File file : fileList) {
                String fileName = file.getName();
                String path = file.getAbsolutePath();
                int index = fileName.indexOf(hash.trim());
                if (index >= 0) {
                    return path;
                }
            }
        }
        return "";
    }

    public static String getLocalSavePath() {
        return Environment.getExternalStorageDirectory().getPath() + "/" +
                MusicApplication.getInstance().getPackageName() + "/";
    }

    public static String getSongSavePath(String hash) {
        return getLocalSavePath()
                + hash.trim() + ".mp3";
    }

}
