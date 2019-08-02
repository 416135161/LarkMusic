package internet.com.larkmusic.player;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

import com.arialyy.aria.core.Aria;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import internet.com.larkmusic.action.ActionDownLoad;
import internet.com.larkmusic.action.ActionPlayEvent;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.util.CommonUtil;
import internet.com.larkmusic.util.FileUtils;

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
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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

    //接收EventBus post过来的PlayEvent
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ActionPlayEvent playEvent) {
        switch (playEvent.getAction()) {
            case PLAY:
                if (playEvent.getQueue() != null && playEvent.getQueue().size() > 0) {
                    MusicPlayer.getPlayer().addQueue(playEvent.getQueue(), true);
                } else {
                    MusicPlayer.getPlayer().play();
                }
                break;
            case NEXT:
                MusicPlayer.getPlayer().next();
                break;
            case PREVIOUS:
                MusicPlayer.getPlayer().previous();
                break;
            case STOP:
                MusicPlayer.getPlayer().pause();
                break;
            case RESUME:
                MusicPlayer.getPlayer().resume();
                break;
            case SEEK:
                MusicPlayer.getPlayer().seekTo(playEvent.getSeekTo());
        }
    }


    @Subscribe
    public void onEventDownLoad(ActionDownLoad actionDownLoad) {
        Song song = actionDownLoad.song;
        String filePath = CommonUtil.getSongSavePath(song.getHash());
        if (!FileUtils.isFileExist(filePath)) {
            Aria.download(this)
                    .load(song.getPlayUrl())     //读取下载地址
                    .setFilePath(filePath)
                    .start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

    }

}