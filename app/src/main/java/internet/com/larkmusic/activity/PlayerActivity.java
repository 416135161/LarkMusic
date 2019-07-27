package internet.com.larkmusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lauzy.freedom.library.Lrc;
import com.lauzy.freedom.library.LrcHelper;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import internet.com.larkmusic.R;
import internet.com.larkmusic.action.PlayerStatus;
import internet.com.larkmusic.action.ActionPlayEvent;
import internet.com.larkmusic.action.ActionPlayerInformEvent;
import internet.com.larkmusic.animations.RotateAnimation;
import internet.com.larkmusic.base.EventActivity;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.fragment.PlayListDialog;
import internet.com.larkmusic.fragment.PlayingListDialog;
import internet.com.larkmusic.player.MusicPlayer;
import internet.com.larkmusic.player.PlayMode;
import internet.com.larkmusic.util.FavoriteService;
import internet.com.larkmusic.util.SpHelper;

/**
 * Created by sjning
 * created on: 2019/5/26 下午2:58
 * description:
 */
public class PlayerActivity extends EventActivity {
    public static final String INTENT_KEY_SONG = "intent_key_song";
    @BindView(R.id.lrc_view)
    com.lauzy.freedom.library.LrcView lrcView;
    @BindView(R.id.tv_song)
    TextView tvSong;
    @BindView(R.id.tv_song1)
    TextView tvSong1;
    @BindView(R.id.tv_singer1)
    TextView tvSinger1;
    @BindView(R.id.tv_singer)
    TextView tvSinger;
    @BindView(R.id.progressBar)
    SeekBar seekBar;
    @BindView(R.id.tv_time_left)
    TextView tvTimeLeft;
    @BindView(R.id.tv_time_right)
    TextView tvTimeRight;
    @BindView(R.id.iv_favorite)
    ImageView ivFavorite;

    @BindView(R.id.iv_play_stop)
    ImageView ivPlayStop;
    @BindView(R.id.iv_play_indicator)
    ImageView ivPlayIndicator;

    @BindView(R.id.iv_recycle)
    ImageView ivRecycle;
    @BindView(R.id.iv_song_bg)
    ImageView ivSongBg;

    @BindView(R.id.view_lrc)
    LinearLayout viewLrc;
    @BindView(R.id.view_top)
    LinearLayout viewTop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ActionPlayEvent actionPlayEvent = new ActionPlayEvent();
                actionPlayEvent.setAction(ActionPlayEvent.Action.SEEK);
                actionPlayEvent.setSeekTo(seekBar.getProgress());
                EventBus.getDefault().post(actionPlayEvent);
            }
        });
        initIvRecycle();
        initView();
    }

    private void initView() {
        if (MusicPlayer.getPlayer() != null && MusicPlayer.getPlayer().getNowPlaying() != null) {
            PlayerStatus playerStatus = MusicPlayer.getPlayer().getStatus();
            if(playerStatus == PlayerStatus.PLAYING){
                return;
            }else{
                ActionPlayerInformEvent actionPlayerInformEvent = new ActionPlayerInformEvent();
                if (playerStatus == PlayerStatus.STOP ) {
                    actionPlayerInformEvent.currentTime = MusicPlayer.getPlayer().getCurrentPosition();
                    actionPlayerInformEvent.duration = MusicPlayer.getPlayer().getDuration();
                }
                actionPlayerInformEvent.action = playerStatus;
                actionPlayerInformEvent.song = MusicPlayer.getPlayer().getNowPlaying();
                onEventPlayerInform(actionPlayerInformEvent);
            }


        }
    }

    void initIvRecycle() {
        int index = SpHelper.getDefault().getInt(SpHelper.KEY_RECYCLE_MODE, PlayMode.LOOP.ordinal());
        if (index == PlayMode.LOOP.ordinal()) {
            MusicPlayer.getPlayer().setPlayMode(PlayMode.LOOP);
            ivRecycle.setImageResource(R.mipmap.icon_recycle_all);
        } else if (index == PlayMode.REPEAT.ordinal()) {
            MusicPlayer.getPlayer().setPlayMode(PlayMode.REPEAT);
            ivRecycle.setImageResource(R.mipmap.icon_recycle_single);
        } else if (index == PlayMode.RANDOM.ordinal()) {
            MusicPlayer.getPlayer().setPlayMode(PlayMode.RANDOM);
            ivRecycle.setImageResource(R.mipmap.icon_recycle_random);
        }
    }

    @OnClick(R.id.view_top)
    void onClickViewTop(View view) {
        viewLrc.setVisibility(View.VISIBLE);
        viewTop.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.view_lrc, R.id.lrc_view})
    void onClickViewLrc(View view) {
        viewLrc.setVisibility(View.INVISIBLE);
        viewTop.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_back)
    void onClickViewBack(View view) {
        finish();
    }

    @OnClick({R.id.iv_favorite})
    void onClickFavorite(View view) {
        if (ivFavorite.getTag(R.id.tag_key_favorite_check) == null) {
            return;
        }
        if ((Boolean) ivFavorite.getTag(R.id.tag_key_favorite_check)) {
            ivFavorite.setImageResource(R.mipmap.icon_favorite_normal);
            ivFavorite.setTag(R.id.tag_key_favorite_check, Boolean.FALSE);
            FavoriteService.getInstance().saveSong(
                    (Song) ivFavorite.getTag(R.id.tag_key_favorite_song), false);
        } else {
            ivFavorite.setImageResource(R.mipmap.icon_favorite_select);
            ivFavorite.setTag(R.id.tag_key_favorite_check, Boolean.TRUE);
            FavoriteService.getInstance().saveSong(
                    (Song) ivFavorite.getTag(R.id.tag_key_favorite_song), true);
        }
    }

    @OnClick({R.id.iv_recycle})
    void onClickRecycle(View view) {
        int index = SpHelper.getDefault().getInt(SpHelper.KEY_RECYCLE_MODE, PlayMode.LOOP.ordinal());
        if (index == PlayMode.LOOP.ordinal()) {
            SpHelper.getDefault().putInt(SpHelper.KEY_RECYCLE_MODE, PlayMode.REPEAT.ordinal());
            MusicPlayer.getPlayer().setPlayMode(PlayMode.REPEAT);
            ivRecycle.setImageResource(R.mipmap.icon_recycle_single);
        } else if (index == PlayMode.REPEAT.ordinal()) {
            SpHelper.getDefault().putInt(SpHelper.KEY_RECYCLE_MODE, PlayMode.RANDOM.ordinal());
            MusicPlayer.getPlayer().setPlayMode(PlayMode.RANDOM);
            ivRecycle.setImageResource(R.mipmap.icon_recycle_random);
        } else if (index == PlayMode.RANDOM.ordinal()) {
            SpHelper.getDefault().putInt(SpHelper.KEY_RECYCLE_MODE, PlayMode.LOOP.ordinal());
            MusicPlayer.getPlayer().setPlayMode(PlayMode.LOOP);
            ivRecycle.setImageResource(R.mipmap.icon_recycle_all);
        }
    }

    @OnClick(R.id.iv_play_stop)
    void onClickPlayStop(View view) {
        ActionPlayEvent actionPlayEvent = new ActionPlayEvent();
        if (MusicPlayer.getPlayer().isPlaying()) {
            actionPlayEvent.setAction(ActionPlayEvent.Action.STOP);
        } else {
            if (MusicPlayer.getPlayer().isPause()) {
                actionPlayEvent.setAction(ActionPlayEvent.Action.RESUME);
            } else {
                actionPlayEvent.setAction(ActionPlayEvent.Action.PLAY);
            }
        }
        EventBus.getDefault().post(actionPlayEvent);
    }

    @OnClick(R.id.iv_last)
    void onClickLast(View view) {
        ActionPlayEvent actionPlayEvent = new ActionPlayEvent();
        actionPlayEvent.setAction(ActionPlayEvent.Action.PREVIOUS);
        EventBus.getDefault().post(actionPlayEvent);
    }

    @OnClick(R.id.iv_next)
    void onClickNext(View view) {
        ActionPlayEvent actionPlayEvent = new ActionPlayEvent();
        actionPlayEvent.setAction(ActionPlayEvent.Action.NEXT);
        EventBus.getDefault().post(actionPlayEvent);
    }

    @OnClick(R.id.iv_list)
    void onClickList(View view) {
        new PlayingListDialog().show(getSupportFragmentManager(), PlayingListDialog.class.getName());
    }

    @OnClick(R.id.iv_playlist)
    void onClickPlayList(View view) {
        new PlayListDialog().setSong(MusicPlayer.getPlayer().getNowPlaying()).show(getSupportFragmentManager(), PlayListDialog.class.getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPlayerInform(ActionPlayerInformEvent event) {
        if (event == null) {
            return;
        }
        if (event.action == PlayerStatus.PLAYING) {
            ivPlayIndicator.setVisibility(View.GONE);
            ivPlayStop.setImageResource(R.mipmap.icon_stop);
            refreshTime(tvTimeLeft, event.currentTime);
            refreshTime(tvTimeRight, event.duration);
            seekBar.setMax(event.duration);
            seekBar.setProgress(event.currentTime);
            initLrc(event.song.getLrc());
            lrcView.updateTime(event.currentTime);
            initFavorite(event.song);
            tvSong.setText(event.song.getSongName());
            tvSong1.setText(event.song.getSongName());
            tvSinger.setText(event.song.getSingerName());
            tvSinger1.setText(event.song.getSingerName());
        } else if (event.action == PlayerStatus.STOP) {
            ivPlayIndicator.setVisibility(View.GONE);
            ivPlayStop.setImageResource(R.mipmap.icon_play);
            refreshTime(tvTimeLeft, event.currentTime);
            refreshTime(tvTimeRight, event.duration);
            seekBar.setMax(event.duration);
            seekBar.setProgress(event.currentTime);
        } else if (event.action == PlayerStatus.PREPARE) {
            RotateAnimation.create().with(ivPlayIndicator)
                    .setDuration(1000)
                    .start();
            ivPlayIndicator.setVisibility(View.VISIBLE);
            tvSong.setText(event.song.getSongName());
            tvSong1.setText(event.song.getSongName());
            tvSinger.setText(event.song.getSingerName());
            tvSinger1.setText(event.song.getSingerName());
            initLrc(event.song.getLrc());
            initFavorite(event.song);
        }
        Song song = event.song;
        if(!TextUtils.isEmpty(song.getImgUrl())){
            Picasso.with(this)
                    .load(song.getImgUrl().replace("90x90", "300x300"))
                    .error(R.mipmap.icon_player_main_default)
                    .placeholder(R.mipmap.icon_player_main_default)
                    .into(ivSongBg);
        }

    }

    void initFavorite(Song song){
        if (FavoriteService.getInstance().isFavorite(song)) {
            ivFavorite.setImageResource(R.mipmap.icon_favorite_select);
            ivFavorite.setTag(R.id.tag_key_favorite_check, Boolean.TRUE);
        } else {
            ivFavorite.setImageResource(R.mipmap.icon_favorite_normal);
            ivFavorite.setTag(R.id.tag_key_favorite_check, Boolean.FALSE);
        }
        ivFavorite.setTag(R.id.tag_key_favorite_song, song);
    }

    protected void initLrc(String lrc) {
        List<Lrc> lrcs = LrcHelper.parseLrcFromString(lrc);
        if (lrcs != null && lrcs.size() > 0 && !TextUtils.equals(lrc, lrcView.getmLrc())) {
            lrcView.setLrcData(lrcs);
            lrcView.setmLrc(lrc);
            lrcView.resume();
        } else {
            lrcView.setEmptyContent(getString(R.string.lrc_nothing));
        }

    }

    public void refreshTime(TextView textView, int millsec) {
        Pair<String, String> temp = getTime(millsec);
        textView.setText(temp.first + ":" + temp.second);

    }

    public Pair<String, String> getTime(int millsec) {
        int min, sec;
        sec = millsec / 1000;
        min = sec / 60;
        sec = sec % 60;
        String minS, secS;
        minS = String.valueOf(min);
        secS = String.valueOf(sec);
        if (sec < 10) {
            secS = "0" + secS;
        }
        Pair<String, String> pair = Pair.create(minS, secS);
        return pair;
    }

    public static void start(Song song, Activity activity) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_KEY_SONG, song);
        intent.setClass(activity, PlayerActivity.class);
        activity.startActivity(intent);
    }
}
