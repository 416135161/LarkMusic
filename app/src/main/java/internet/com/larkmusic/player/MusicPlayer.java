package internet.com.larkmusic.player;

import android.media.MediaPlayer;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindCallback;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionDownLoad;
import internet.com.larkmusic.action.ActionPlayerInformEvent;
import internet.com.larkmusic.action.PlayerStatus;
import internet.com.larkmusic.app.MusicApplication;
import internet.com.larkmusic.bean.SavedStateBean;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.network.GetSongCallBack;
import internet.com.larkmusic.util.CloudDataUtil;
import internet.com.larkmusic.util.CommonUtil;
import internet.com.larkmusic.util.FileUtils;
import internet.com.larkmusic.util.RecentSongService;

/**
 * Created by MASAILA on 16/5/13.
 */
public class MusicPlayer implements MediaPlayer.OnCompletionListener {

    private static MusicPlayer player;

    private PlayerStatus status = PlayerStatus.STOP;
    private ManagedMediaPlayer mMediaPlayer;
    private LinkedList<Song> mQueue;
    private int mQueueIndex;
    private PlayMode mPlayMode;

    private Timer timer;

    public static MusicPlayer getPlayer() {
        if (player == null) {
            player = new MusicPlayer();
        }
        return player;
    }

    private MusicPlayer() {

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

        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return false;
            }
        });

        mQueue = new LinkedList<>();
        mQueueIndex = 0;

        mPlayMode = PlayMode.LOOP;
        initSavedState();
    }

    private void initSavedState() {
        LitePal.where("tag = ?", SavedStateBean.TAG_STATE).findFirstAsync(SavedStateBean.class).listen(new FindCallback<SavedStateBean>() {
            @Override
            public void onFinish(SavedStateBean savedStateBean) {
                if (savedStateBean != null) {
                    mQueueIndex = savedStateBean.getIndex();
                    List<Song> songList = new Gson().fromJson(savedStateBean.getCurrentPlayList(), new TypeToken<List<Song>>() {
                    }.getType());
                    if (songList != null && songList.size() > 0) {
                        mQueue.addAll(songList);
                    }
                }
            }
        });

    }

    public void addQueue(List<Song> songs, boolean playNow) {
        if (songs == null || songs.size() == 0)
            return;
        for (Song item : songs) {
            deleteIfExist(item);
        }
        if (playNow) {
            initQueue();
            //一次加入多首歌曲
            if (songs.size() > 1) {
                mQueue.addAll(0, songs);
                mQueueIndex = 0;
                stop();
                play(getNowPlaying());
            } else { //单曲选择播放
                play(songs.get(0));
            }
        } else {
            mQueue.addAll(songs);
        }
        saveCurrentState();
    }

    public void deleteIfExist(Song song) {
        if (song == null || mQueue == null || mQueue.size() == 0) {
            return;
        }
        for (int i = mQueue.size() - 1; i >= 0; i--) {
            if (TextUtils.equals(song.getHash(), mQueue.get(i).getHash())) {
                mQueue.remove(i);
                saveCurrentState();
                return;
            }
        }

    }

    public void addQueueNext(Song song) {
        if (song == null) {
            return;
        }
        initQueue();
        deleteIfExist(song);
        if (mQueue.size() > mQueueIndex) {
            mQueue.add(mQueueIndex + 1, song);
        } else {
            mQueue.addLast(song);
        }
        saveCurrentState();
    }

    public void addQueueLater(Song song) {
        if (song == null) {
            return;
        }
        initQueue();
        deleteIfExist(song);
        mQueue.addLast(song);
        saveCurrentState();
    }

    private void initQueue() {
        if (mQueue == null) {
            mQueue = new LinkedList<>();
        }
    }

    public void play() {
        stop();
        play(getNowPlaying());
    }

    private void play(final Song song) {
        if (song == null) {
            return;
        }
        //如果是本地歌曲直接播放
        if (song.isLocal() && FileUtils.isFileExist(song.getPlayUrl())) {
            doPlay(song);
        } else {
            //先看是否已经下载过此歌曲，1、如果下载过则播放本地歌曲，2、如果没有则去服务器请求下载地址下载并播放
            String filePath = CommonUtil.getSongSavePath(song.getHash());
            if (FileUtils.isFileExist(filePath)) {
                song.setPlayUrl(filePath);
                LitePal.where("hash = ?", song.getHash()).findFirstAsync(Song.class).listen(new FindCallback<Song>() {
                    @Override
                    public void onFinish(Song songDB) {
                        if (songDB != null) {
                            song.setImgUrl(songDB.getImgUrl());
                            song.setPortrait(songDB.getPortrait());
                            song.setLrc(songDB.getLrc());
                        }
                        doPlay(song);
                        CloudDataUtil.checkImageAndLrc(song);
                    }
                });
            } else {
                CloudDataUtil.getSongPlayUrl(song, new GetSongCallBack() {
                    @Override
                    public void onSongGetOk(Song song) {
                        doPlay(song);
                        EventBus.getDefault().post(new ActionDownLoad(song));
                    }

                    @Override
                    public void onSongGetFail() {
                        deleteIfExist(song);
                        saveCurrentState();
                        //如果队列里面还有歌曲且处于停止状态，则播放下一首
                        if (mQueue.size() > 0 && !isPlaying()) {
                            play();
                        }
                        Toast.makeText(MusicApplication.getInstance(), MusicApplication.getInstance().getString(R.string.can_not_play), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void doPlay(Song song) {
        refreshQueue(song);
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(song.getPlayUrl());
            mMediaPlayer.prepareAsync();
            sendPlayerInformation(PlayerStatus.PREPARE);
            cancelTimer();
            if (!song.isLocal()) {
                RecentSongService.getInstance().saveSong(song);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void stop() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            cancelTimer();
            sendPlayerInformation(PlayerStatus.STOP);
        }
    }

    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
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
        stop();
        play(getNextSong());
    }

    public void previous() {
        stop();
        play(getPreviousSong());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }

    public Song getNowPlaying() {
        if (mQueue.isEmpty() || mQueueIndex < 0 || mQueueIndex >= mQueue.size()) {
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
        if (mQueue == null || mQueue.size() == 0) {
            return 0;
        }
        mQueueIndex = (mQueueIndex + 1) % mQueue.size();
        return mQueueIndex;
    }

    private int getPreviousIndex() {
        if (mQueue.size() == 1) {
            return 0;
        }
        mQueueIndex = (mQueueIndex - 1) % mQueue.size();
        if (mQueueIndex < 0 || (mQueueIndex - 1) >= mQueue.size()) {
            mQueueIndex = 0;
        }
        return mQueueIndex;
    }

    private int getRandomIndex() {
        mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
        return mQueueIndex;
    }

    public void release() {
        saveCurrentState();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        mMediaPlayer = null;
        player = null;
    }

    private void saveCurrentState() {
        if (mQueue != null && mQueue.size() > 0) {
            SavedStateBean currentStateBean = new SavedStateBean();
            currentStateBean.setTag(SavedStateBean.TAG_STATE);
            currentStateBean.setIndex(MusicPlayer.getPlayer().mQueueIndex);
            currentStateBean.setCurrentPlayList(new Gson().toJson(mQueue));
            currentStateBean.saveOrUpdate("tag = ?", SavedStateBean.TAG_STATE);
        }
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
            try {
                actionPlayerInformEvent.currentTime = mMediaPlayer.getCurrentPosition();
                actionPlayerInformEvent.duration = mMediaPlayer.getDuration();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        actionPlayerInformEvent.action = action;
        actionPlayerInformEvent.song = getNowPlaying();
        EventBus.getDefault().post(actionPlayerInformEvent);
    }


    public PlayerStatus getStatus() {
        return status;
    }

    public LinkedList<Song> getQueue() {
        return mQueue;
    }

    private void refreshQueue(Song song) {
        initQueue();
        for (int i = 0; i < mQueue.size(); i++) {
            if (TextUtils.equals(song.getHash(), mQueue.get(i).getHash())) {
                mQueueIndex = i;
                return;
            }
        }
        mQueue.add(0, song);
        mQueueIndex = 0;
    }

}