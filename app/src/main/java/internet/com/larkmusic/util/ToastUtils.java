package internet.com.larkmusic.util;

import android.os.Build;
import androidx.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

import internet.com.larkmusic.app.MusicApplication;


/**
 * Toast 工具类
 */
public class ToastUtils {
    private static Toast lastToast;

    public static void show(@StringRes int resourceId) {
        show(MusicApplication.getInstance().getResources().getString(resourceId), Toast.LENGTH_SHORT);
    }

    public static void show(@StringRes int resourceId, int duration) {
        show(MusicApplication.getInstance().getResources().getString(resourceId), duration);
    }

    public static void show(String message) {
        show(message, Toast.LENGTH_SHORT);
    }

    public static void show(String message, int duration) {
        if (TextUtils.isEmpty(message)) return;

        // 9.0 以上直接用调用即可防止重复的显示的问题，且如果复用 Toast 会出现无法再出弹出对话框问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Toast.makeText(MusicApplication.getInstance(), message, duration).show();
        } else {
            if (lastToast != null) {
                lastToast.setText(message);
            } else {
                lastToast = Toast.makeText(MusicApplication.getInstance(), message, duration);
            }
            lastToast.show();
        }
    }
}
