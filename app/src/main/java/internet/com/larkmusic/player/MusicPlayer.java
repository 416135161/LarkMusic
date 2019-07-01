package internet.com.larkmusic.player;

import android.content.Context;
import android.media.MediaPlayer;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import internet.com.larkmusic.action.ActionPlayerInformEvent;
import internet.com.larkmusic.action.PlayerStatus;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.network.GetSongCallBack;
import internet.com.larkmusic.util.CloudDataUtil;
import internet.com.larkmusic.util.RecentSongService;

/**
 * Created by MASAILA on 16/5/13.
 */
public class MusicPlayer implements MediaPlayer.OnCompletionListener {

    private static MusicPlayer player = new MusicPlayer();

    private PlayerStatus status = PlayerStatus.STOP;
    private ManagedMediaPlayer mMediaPlayer;
    private Context mContext;
    private LinkedList<Song> mQueue;
    private int mQueueIndex;
    private PlayMode mPlayMode;

    private Timer timer;

    public static MusicPlayer getPlayer() {
        return player;
    }

    public static void setPlayer(MusicPlayer player) {
        MusicPlayer.player = player;
    }

    public MusicPlayer() {

        mMediaPlayer = new ManagedMediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                informPlayingState();
            }
        });
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer.start();
                informPlayingState();
            }
        });

        mQueue = new LinkedList<>();
        mQueueIndex = 0;

        mPlayMode = PlayMode.LOOP;
    }

    public void addQueue(List<Song> songs, boolean playNow) {
        if (songs == null || songs.size() == 0)
            return;
        if (playNow) {
            initQueue();
            mQueue.addAll(0, songs);
            mQueueIndex = 0;
            play(getNowPlaying());
        } else {
            mQueue.addAll(songs);
        }

    }

    public void addQueueNext(Song song) {
        if (song == null) {
            return;
        }
        initQueue();
        mQueue.add(getNextIndex(), song);
    }

    public void addQueueLater(Song song) {
        if (song == null) {
            return;
        }
        initQueue();
        mQueue.addLast(song);
    }

    private void initQueue() {
        if (mQueue == null) {
            mQueue = new LinkedList<>();
        }
    }

    public void play() {
        play(getNowPlaying());
    }

    private void play(final Song song) {
        CloudDataUtil.getSongFromCloud(song, new GetSongCallBack() {
            @Override
            public void onSongGetOk() {
                try {
                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(song.getPlayUrl());
                    mMediaPlayer.prepareAsync();
                    sendPlayerInformation(PlayerStatus.PREPARE);
                    cancelTimer();
                    RecentSongService.getInstance().saveSong(song);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSongGetFail() {

            }
        });

    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            cancelTimer();
            sendPlayerInformation(PlayerStatus.STOP);
        }
    }

    public void resume() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
            informPlayingState();
        }
    }

    public void next() {
        play(getNextSong());
    }

    public void previous() {
        play(getPreviousSong());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }

    public Song getNowPlaying() {
        if (mQueue.isEmpty()) {
            return null;
        }
        return mQueue.get(mQueueIndex);
    }

    private Song getNextSong() {
        if (mQueue.isEmpty()) {
            return null;
        }
        switch (mPlayMode) {
            case LOOP:
                return mQueue.get(getNextIndex());
            case RANDOM:
                return mQueue.get(getRandomIndex());
            case REPEAT:
                return mQueue.get(mQueueIndex);
        }
        return null;
    }

    private Song getPreviousSong() {
        if (mQueue.isEmpty()) {
            return null;
        }
        switch (mPlayMode) {
            case LOOP:
                return mQueue.get(getPreviousIndex());
            case RANDOM:
                return mQueue.get(getRandomIndex());
            case REPEAT:
                return mQueue.get(mQueueIndex);
        }
        return null;
    }


    public int getCurrentPosition() {
        if (getNowPlaying() != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getDuration() {
        if (getNowPlaying() != null) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    public PlayMode getPlayMode() {
        return mPlayMode;
    }

    public void seekTo(int seekTo) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(seekTo);
            sendPlayerInformation(PlayerStatus.PREPARE);
        }
    }

    public void setPlayMode(PlayMode playMode) {
        mPlayMode = playMode;
    }

    private int getNextIndex() {
        mQueueIndex = (mQueueIndex + 1) % mQueue.size();
        return mQueueIndex;
    }

    private int getPreviousIndex() {
        mQueueIndex = (mQueueIndex - 1) % mQueue.size();
        return mQueueIndex;
    }

    private int getRandomIndex() {
        mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
        return mQueueIndex;
    }

    private void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        mMediaPlayer = null;
        mContext = null;
    }

    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    public boolean isPause() {
        if (mMediaPlayer != null && mMediaPlayer.getState() == ManagedMediaPlayer.Status.PAUSED) {
            return true;
        } else {
            return false;
        }
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void informPlayingState() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        if (mMediaPlayer != null) {
                            sendPlayerInformation(PlayerStatus.PLAYING);
                        }

                    }
                }, 0, 1000);
    }

    private void sendPlayerInformation(PlayerStatus action) {
        status = action;
        ActionPlayerInformEvent actionPlayerInformEvent = new ActionPlayerInformEvent();
        if (action == PlayerStatus.STOP || action == PlayerStatus.PLAYING) {
            actionPlayerInformEvent.currentTime = mMediaPlayer.getCurrentPosition();
            actionPlayerInformEvent.duration = mMediaPlayer.getDuration();
        }
        actionPlayerInformEvent.action = action;
        actionPlayerInformEvent.song = getNowPlaying();
        EventBus.getDefault().post(actionPlayerInformEvent);
    }


    public PlayerStatus getStatus() {
        return status;
    }
}