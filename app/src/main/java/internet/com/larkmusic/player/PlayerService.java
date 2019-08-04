package internet.com.larkmusic.player;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

public class PlayerService extends Service {
    public static final String PREVIOUS_ACTION = "com.lark.music.previous";
    public static final String NEXT_ACTION = "com.lark.music.next";
    public static final String TOGGLE_PAUSE_ACTION = "com.lark.music.toggle_pause";

    public static final int NOTICE_ID = 100;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

        } else {
            startForeground(NOTICE_ID, new Notification());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            handleAction(action);
        }
        return START_STICKY;
    }


    private void handleAction(String action) {
        if (TextUtils.equals(action, PREVIOUS_ACTION)) {
            MusicPlayer.getPlayer().previous();
        } else if (TextUtils.equals(action, NEXT_ACTION)) {
            MusicPlayer.getPlayer().next();
        } else if (TextUtils.equals(action, TOGGLE_PAUSE_ACTION)) {
            if (MusicPlayer.getPlayer().isPlaying()) {
                MusicPlayer.getPlayer().pause();
            } else {
                MusicPlayer.getPlayer().resume();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}