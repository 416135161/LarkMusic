package internet.com.larkmusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import internet.com.larkmusic.action.ActionPlayEvent;
import internet.com.larkmusic.action.ActionPlayerInformEvent;
import internet.com.larkmusic.animations.RotateAnimation;
import internet.com.larkmusic.base.EventActivity;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.player.MusicPlayer;

/**
 * Created by sjning
 * created on: 2019/5/26 下午2:58
 * description:
 */
public class PlayerActivity extends EventActivity {
    public static final String INTENT_KEY_SONG = "intent_key_song";
    @BindView(R.id.lrc_view)
    com.lauzy.freedom.library.LrcView lrcView;
    @BindView(R.id.iv_singer)
    ImageView ivSinger;
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
    FrameLayout viewTop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventStartPlayAct(ActionPlayerInformEvent event) {
        if (event == null) {
            return;
        }
        if (event.action == ActionPlayerInformEvent.Action.PLAYING) {
            ivPlayIndicator.setVisibility(View.GONE);
            ivPlayStop.setImageResource(R.mipmap.icon_stop);
            refreshTime(tvTimeLeft, event.currentTime);
            refreshTime(tvTimeRight, event.duration);
            seekBar.setMax(event.duration);
            seekBar.setProgress(event.currentTime);
            lrcView.updateTime(event.currentTime);
        } else if (event.action == ActionPlayerInformEvent.Action.STOP) {
            ivPlayIndicator.setVisibility(View.GONE);
            ivPlayStop.setImageResource(R.mipmap.icon_play);
            refreshTime(tvTimeLeft, event.currentTime);
            refreshTime(tvTimeRight, event.duration);
            seekBar.setMax(event.duration);
            seekBar.setProgress(event.currentTime);
        } else if (event.action == ActionPlayerInformEvent.Action.PREPARE) {
            RotateAnimation.create().with(ivPlayIndicator)
                    .setDuration(1000)
                    .start();
            ivPlayIndicator.setVisibility(View.VISIBLE);
            tvSong.setText(event.song.getSongName());
            tvSong1.setText(event.song.getSongName());
            tvSinger.setText(event.song.getSingerName());
            tvSinger1.setText(event.song.getSingerName());
            initLrc(event.song.getLrc());
        }
        Song song = event.song;
        Picasso.with(this)
                .load(song.getPortrait())
                .error(R.mipmap.ic_singer_default)
                .placeholder(R.mipmap.ic_singer_default)
                .into(ivSinger);
        Picasso.with(this)
                .load(song.getImgUrl())
                .error(R.mipmap.icon_player_main_default)
                .placeholder(R.mipmap.icon_player_main_default)
                .into(ivSongBg);
    }

    protected void initLrc(String lrc) {
        List<Lrc> lrcs = LrcHelper.parseLrcFromString(lrc);
        if (lrcs != null && lrcs.size() > 0) {
            lrcView.setLrcData(lrcs);
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
