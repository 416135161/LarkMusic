package internet.com.larkmusic.player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.arialyy.aria.core.Aria;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionDownLoad;
import internet.com.larkmusic.action.ActionPlayEvent;
import internet.com.larkmusic.action.ActionPlayerInformEvent;
import internet.com.larkmusic.action.PlayerStatus;
import internet.com.larkmusic.activity.MainActivity;
import internet.com.larkmusic.activity.PlayerActivity;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.receiver.MediaButtonIntentReceiver;
import internet.com.larkmusic.util.CommonUtil;
import internet.com.larkmusic.util.FileUtils;

public class PlayerService extends Service {
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
        registerPhoneStateListener();
        registerHeadsetReceiver(this);
        initNotification();
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
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        unregisterHeadsetReceiver(this);
    }

    private void registerPhoneStateListener() {
        CustomPhoneStateListener customPhoneStateListener = new CustomPhoneStateListener(this);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            telephonyManager.listen(customPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    public void registerHeadsetReceiver(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);// 另说context.AUDIO_SERVICE
        ComponentName name = new ComponentName(context.getPackageName(), MediaButtonIntentReceiver.class.getName());// 另说MediaButtonReceiver.class.getName()
        audioManager.registerMediaButtonEventReceiver(name);
    }

    public void unregisterHeadsetReceiver(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        ComponentName name = new ComponentName(context.getPackageName(), MediaButtonIntentReceiver.class.getName());
        audioManager.unregisterMediaButtonEventReceiver(name);
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

        contentViews.setOnClickPendingIntent(R.id.iv_play_stop, retrievePlaybackAction(TOGGLE_PAUSE_ACTION));
        contentViews.setOnClickPendingIntent(R.id.iv_last, retrievePlaybackAction(PREVIOUS_ACTION));
        contentViews.setOnClickPendingIntent(R.id.iv_next, retrievePlaybackAction(NEXT_ACTION));

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
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
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

    public static final String PREVIOUS_ACTION = "com.lark.music.previous";
    public static final String NEXT_ACTION = "com.lark.music.next";
    public static final String TOGGLE_PAUSE_ACTION = "com.lark.music.toggle_pause";

}