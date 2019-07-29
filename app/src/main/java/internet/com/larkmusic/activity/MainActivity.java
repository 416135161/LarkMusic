package internet.com.larkmusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionBack;
import internet.com.larkmusic.action.ActionPlayEvent;
import internet.com.larkmusic.action.ActionPlayerInformEvent;
import internet.com.larkmusic.action.ActionSelectSong;
import internet.com.larkmusic.action.ActionShowOperateDlg;
import internet.com.larkmusic.action.PlayerStatus;
import internet.com.larkmusic.animations.RotateAnimation;
import internet.com.larkmusic.back.BackHandlerHelper;
import internet.com.larkmusic.base.AdsBaseActivity;
import internet.com.larkmusic.base.BaseActivity;
import internet.com.larkmusic.base.MainBaseActivity;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.fragment.HallFragment;
import internet.com.larkmusic.fragment.LibraryFragment;
import internet.com.larkmusic.fragment.MeFragment;
import internet.com.larkmusic.fragment.OperateDialog;
import internet.com.larkmusic.fragment.PlayingListDialog;
import internet.com.larkmusic.fragment.SearchFragment;
import internet.com.larkmusic.network.Config;
import internet.com.larkmusic.network.netnew.NewCloudDataUtil;
import internet.com.larkmusic.player.MusicPlayer;
import internet.com.larkmusic.player.PlayerService;
import internet.com.larkmusic.util.CloudDataUtil;
import internet.com.larkmusic.util.SpHelper;

public class MainActivity extends MainBaseActivity {

    @BindView(R.id.iv_hall)
    ImageView ivHall;
    @BindView(R.id.tv_hall)
    TextView tvHall;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_library)
    ImageView ivLibrary;
    @BindView(R.id.tv_library)
    TextView tvLibrary;
    @BindView(R.id.iv_me)
    ImageView ivMe;
    @BindView(R.id.tv_me)
    TextView tvMe;

    //当前显示的Fragment
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        onClickViewHall();
        initSavedState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent();
        intent.setClass(this, PlayerService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayer.getPlayer().release();
    }

    private void initSavedState() {
        if (MusicPlayer.getPlayer().getNowPlaying() != null && !MusicPlayer.getPlayer().isPlaying()) {
            ActionPlayerInformEvent actionPlayerInformEvent = new ActionPlayerInformEvent();
            actionPlayerInformEvent.song = MusicPlayer.getPlayer().getNowPlaying();
            actionPlayerInformEvent.action = PlayerStatus.STOP;
            onEventPlayerInform(actionPlayerInformEvent);
        }
    }

    @OnClick(R.id.view_hall)
    void onClickViewHall() {
        setAllNormal();
        ivHall.setImageResource(R.mipmap.tab_hall_select);
        tvHall.setTextColor(getResources().getColor(R.color.text_red));
        startFragment(HallFragment.class, new Bundle());
    }

    @OnClick(R.id.view_search)
    void onClickViewSearch() {
        setAllNormal();
        ivSearch.setImageResource(R.mipmap.tab_search_select);
        tvSearch.setTextColor(getResources().getColor(R.color.text_red));
        tvSearch.setTypeface(Config.tfLark);
        startFragment(SearchFragment.class, new Bundle());
        showAd();
    }

    @OnClick(R.id.view_library)
    void onClickViewLibrary() {
        setAllNormal();
        ivLibrary.setImageResource(R.mipmap.tab_library_select);
        tvLibrary.setTextColor(getResources().getColor(R.color.text_red));
        startFragment(LibraryFragment.class, new Bundle());
        showAd();
    }

    @OnClick(R.id.view_me)
    void onClickViewMe() {
        setAllNormal();
        ivMe.setImageResource(R.mipmap.tab_me_select);
        tvMe.setTextColor(getResources().getColor(R.color.text_red));
        startFragment(MeFragment.class, new Bundle());
    }

    private void setAllNormal() {
        ivHall.setImageResource(R.mipmap.tab_hall_normal);
        ivSearch.setImageResource(R.mipmap.tab_search_normal);
        ivLibrary.setImageResource(R.mipmap.tab_library_normal);
        ivMe.setImageResource(R.mipmap.tab_me_normal);
        tvHall.setTextColor(getResources().getColor(R.color.text_999));
        tvSearch.setTextColor(getResources().getColor(R.color.text_999));
        tvLibrary.setTextColor(getResources().getColor(R.color.text_999));
        tvMe.setTextColor(getResources().getColor(R.color.text_999));
    }


    //隐藏当前的Fragment,同时显示指定的Fragment
    private void startFragment(Class<? extends Fragment> fragmentClass, Bundle bundle) {

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(fragmentClass.getSimpleName());
        if (fragment != null) {
            if (fragment == mCurrentFragment) {
                return;
            }
            FragmentTransaction transition = fm.beginTransaction();
            transition.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (mCurrentFragment != null) {
                transition.hide(mCurrentFragment);
            }
            transition.show(fragment);
            transition.commitAllowingStateLoss();
            mCurrentFragment = fragment;
        } else {
            try {
                fragment = fragmentClass.newInstance();
                fragment.setArguments(bundle);
                FragmentTransaction transition = fm.beginTransaction();
                transition.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                if (mCurrentFragment != null) {
                    transition.hide(mCurrentFragment);
                }
                transition.add(R.id.view_container, fragment, fragmentClass.getSimpleName());
                transition.commitAllowingStateLoss();
                mCurrentFragment = fragment;
            } catch (Exception e) {
                Log.e("main", "add fragment fail:" + e);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            if (mCurrentFragment != null && !(mCurrentFragment instanceof HallFragment)) {
                onClickViewHall();
            } else {
                if (SpHelper.getDefault().getBoolean(SpHelper.KEY_STAR)) {
                    doFinish();
                } else {
                    showStarDialog();
                }

            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectSong(ActionSelectSong event) {
        List<Song> songList = new ArrayList<>();
        if (event.song != null) {
            songList.add(event.song);
        }
        if (event.songList != null && event.songList.size() > 0) {
            songList.addAll(event.songList);
        }

        ActionPlayEvent actionPlayEvent = new ActionPlayEvent();
        actionPlayEvent.setAction(ActionPlayEvent.Action.PLAY);
        actionPlayEvent.setQueue(songList);
        EventBus.getDefault().post(actionPlayEvent);
        showAd();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventShowOperateDlg(ActionShowOperateDlg event) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        OperateDialog operateDialog = new OperateDialog();
        operateDialog.setSong(event.song);
        operateDialog.show(fragmentManager, OperateDialog.class.getName());
    }

    @BindView(R.id.view_player)
    View viewPlayer;
    @BindView(R.id.iv_singer)
    ImageView ivSinger;
    @BindView(R.id.iv_singer_indicator)
    ImageView ivSingerIndicator;
    @BindView(R.id.tv_song)
    TextView tvSong;
    @BindView(R.id.iv_play_stop)
    ImageView ivPlayStop;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.iv_list)
    ImageView ivList;

    @OnClick({R.id.iv_play_stop, R.id.iv_next, R.id.iv_singer, R.id.tv_song, R.id.iv_list})
    public void onClickPlayPanel(View view) {
        ActionPlayEvent actionPlayEvent;
        switch (view.getId()) {
            case R.id.iv_singer:
            case R.id.tv_song:
                PlayerActivity.start(null, this);
                break;
            case R.id.iv_next:
                actionPlayEvent = new ActionPlayEvent();
                actionPlayEvent.setAction(ActionPlayEvent.Action.NEXT);
                EventBus.getDefault().post(actionPlayEvent);
                break;
            case R.id.iv_play_stop:
                actionPlayEvent = new ActionPlayEvent();
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
                break;
            case R.id.iv_list:
                new PlayingListDialog().show(getSupportFragmentManager(), PlayingListDialog.class.getName());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPlayerInform(ActionPlayerInformEvent event) {
        if (event == null) {
            return;
        }
        if (viewPlayer.getVisibility() == View.GONE) {
            viewPlayer.setVisibility(View.VISIBLE);
            ivSinger.setVisibility(View.VISIBLE);
        }
        if (event.action == PlayerStatus.PLAYING) {
            ivPlayStop.setImageResource(R.mipmap.ic_home_pause);
            tvSong.setText(event.song.getSongName());
            ivSingerIndicator.setVisibility(View.VISIBLE);
            Animation animation = ivSingerIndicator.getAnimation();

        } else if (event.action == PlayerStatus.STOP) {
            ivPlayStop.setImageResource(R.mipmap.ic_home_play);
            tvSong.setText(event.song.getSongName());
            ivSingerIndicator.setVisibility(View.GONE);
        } else if (event.action == PlayerStatus.PREPARE) {
            RotateAnimation.create().with(ivSingerIndicator)
                    .setDuration(6000)
                    .start();

            tvSong.setText(event.song.getSongName());
            ivSingerIndicator.setVisibility(View.VISIBLE);
        }
        Song song = event.song;
        Picasso.with(this)
                .load(song.getImgUrl())
                .error(R.mipmap.ic_singer_default)
                .placeholder(R.mipmap.ic_singer_default)
                .into(ivSinger);
        Picasso.with(this)
                .load(song.getImgUrl())
                .error(R.mipmap.ic_singer_default)
                .placeholder(R.mipmap.ic_singer_default)
                .into(ivSingerIndicator);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBack(ActionBack event) {
        onBackPressed();
    }

}
