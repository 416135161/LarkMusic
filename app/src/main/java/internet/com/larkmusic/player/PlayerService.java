package internet.com.larkmusic.player;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Timer;

import internet.com.larkmusic.action.ActionPlayEvent;

public class PlayerService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}