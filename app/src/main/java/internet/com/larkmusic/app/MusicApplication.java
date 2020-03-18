package internet.com.larkmusic.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.RemoteViews;

import com.arialyy.aria.core.Aria;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.LitePal;

import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionDownLoad;
import internet.com.larkmusic.action.ActionPlayEvent;
import internet.com.larkmusic.action.ActionPlayerInformEvent;
import internet.com.larkmusic.action.PlayerStatus;
import internet.com.larkmusic.activity.PlayerActivity;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.network.Config;
import internet.com.larkmusic.player.CustomPhoneStateListener;
import internet.com.larkmusic.player.MusicPlayer;
import internet.com.larkmusic.player.PlayerService;
import internet.com.larkmusic.util.CommonUtil;
import internet.com.larkmusic.util.FileUtils;

/**
 * Created by sjning
 * created on: 2019/5/13 下午4:25
 * description:
 */
public class MusicApplication extends Application {

    private static MusicApplication instance;

    private boolean isFirst = true;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        //初始化通知栏
        initNotification();
        //初始化数据库
        LitePal.initialize(this);
        //初始化字体
        try {
            Config.tf4 = Typeface.createFromAsset(getAssets(), "fonts/Intro_Cond_Light.otf");
            Config.tf3 = Typeface.createFromAsset(getAssets(), "fonts/Gidole-Regular.ttf");
            Config.tfLark = Typeface.createFromAsset(getAssets(), "fonts/Century_Gothic_W02.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //手机通话状态监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerPhoneStateListener();
        }
        isFirst = true;
    }

    @Override
    public void onTerminate() {
        clearNotification();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onTerminate();
    }

    public void clearNotification(){
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }
    }

    private void registerPhoneStateListener() {
        CustomPhoneStateListener customPhoneStateListener = new CustomPhoneStateListener(this);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            telephonyManager.listen(customPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    public static MusicApplication getInstance() {
        return instance;
    }


    @Subscribe
    public void onEventPlayerInform(ActionPlayerInformEvent event) {
        if (event == null) {
            return;
        }
        if (event.action == PlayerStatus.PLAYING) {
            updateNotification(event.song);
        } else if (event.action == PlayerStatus.STOP) {
            updateNotification(event.song);
        } else if (event.action == PlayerStatus.PREPARE) {
            updateNotification(event.song);
        }

    }

    //接收EventBus post过来的PlayEvent
    @Subscribe
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

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    String id = "lark_channel_01";
    String name = "lark_channel";

    private void initNotification() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void updateNotification(Song song) {
        int notificationId = 7654322;
        int playButtonResId = MusicPlayer.getPlayer().isPlaying()
                ? R.mipmap.ic_pause_white_36dp : R.mipmap.ic_play_white_36dp;

        RemoteViews contentViews = new RemoteViews(getPackageName(),
                R.layout.custom_notification);
        //通过控件的Id设置属性
        contentViews.setImageViewResource(R.id.iv_singer, R.mipmap.ic_notifity);
        contentViews.setTextViewText(R.id.tv_song, song.getSongName());
        contentViews.setTextViewText(R.id.tv_singer, song.getSingerName());
        contentViews.setImageViewResource(R.id.iv_play_stop, playButtonResId);

        contentViews.setOnClickPendingIntent(R.id.iv_play_stop, retrievePlaybackAction(PlayerService.TOGGLE_PAUSE_ACTION));
        contentViews.setOnClickPendingIntent(R.id.iv_last, retrievePlaybackAction(PlayerService.PREVIOUS_ACTION));
        contentViews.setOnClickPendingIntent(R.id.iv_next, retrievePlaybackAction(PlayerService.NEXT_ACTION));

        Intent intent = new Intent(this, PlayerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // 8.0之后需要传入一个 channelId
            NotificationChannel channel = mNotificationManager.getNotificationChannel(id);
            if (channel == null) {
                channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_MIN);
                mNotificationManager.createNotificationChannel(channel);
            }
            channel.setSound(null, null);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setLightColor(getColor(R.color.colorPrimary));
        }
        mBuilder = new NotificationCompat.Builder(this, id);
        mBuilder.setSmallIcon(R.mipmap.ic_notifity)
                .setTicker("Music");
        mBuilder.setSound(null)
                .setLights(0, 0, 0)
                .setVibrate(null);
        mBuilder.setAutoCancel(false);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setContent(contentViews);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

    private final PendingIntent retrievePlaybackAction(final String action) {
        final ComponentName serviceName = new ComponentName(this, PlayerService.class);
        Intent intent = new Intent(action);
        intent.setComponent(serviceName);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }
}
