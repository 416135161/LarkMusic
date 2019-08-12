package internet.com.larkmusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.alin.lib.bannerlib.listener.OnBannerClickListener;
import com.alin.lib.bannerlib.view.BannerImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionHotSongs;
import internet.com.larkmusic.action.ActionNewSongs;
import internet.com.larkmusic.back.BackHandlerHelper;
import internet.com.larkmusic.back.FragmentBackHandler;
import internet.com.larkmusic.base.EventFragment;
import internet.com.larkmusic.bean.SavedStateBean;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.network.Config;
import internet.com.larkmusic.network.netnew.NewCloudDataUtil;
import internet.com.larkmusic.network.netnew.bean.BillBoardMusicListRequest;
import internet.com.larkmusic.util.CommonUtil;
import internet.com.larkmusic.util.ToastUtils;
import internet.com.larkmusic.view.HotNewView;

/**
 * Created by sjning
 * created on: 2019/5/6 上午11:03
 * description:
 */
public class HallFragment extends EventFragment implements FragmentBackHandler {
    List<Integer> mList;
    List<String> mTitles;

    @BindView(R.id.banner_bv)
    com.alin.lib.bannerlib.BannerView mBannerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.new0)
    HotNewView new0;
    @BindView(R.id.new1)
    HotNewView new1;
    @BindView(R.id.new2)
    HotNewView new2;
    @BindView(R.id.new3)
    HotNewView new3;
    @BindView(R.id.new4)
    HotNewView new4;
    @BindView(R.id.new5)
    HotNewView new5;
    @BindView(R.id.hot0)
    HotNewView hot0;
    @BindView(R.id.hot1)
    HotNewView hot1;
    @BindView(R.id.hot2)
    HotNewView hot2;
    @BindView(R.id.hot3)
    HotNewView hot3;
    @BindView(R.id.hot4)
    HotNewView hot4;
    @BindView(R.id.hot5)
    HotNewView hot5;

    ArrayList<Song> mHotList;
    ArrayList<Song> mNewList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hall;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRefreshLayout();
        mList = new ArrayList<>();                  //ImageView数据
        mList.add(R.mipmap.banner_default);
        mTitles = new ArrayList<>();                //title数据
        mTitles.add("");
        mBannerView.setImages(mList)        //ImageView数据
                .setIndcatorTitles(mTitles) //title数据
                .setAutoPlay(false)
                .start();
        mBannerView.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onBannerClickListener(BannerImageView imageView, Object model, int position) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                HotNewListFragment.TYPE = HotNewListFragment.TYPE_HOT;
                Fragment fragment = new HotNewListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", Config.FROM == Config.FROM_JAPAN ? Config.FROM_JAPAN : Config.FROM_US);
                bundle.putString("rankId", Config.FROM == Config.FROM_JAPAN ? BillBoardMusicListRequest.RANK_Japan_Public
                        : BillBoardMusicListRequest.RANK_BillBoard);
                fragment.setArguments(bundle);
                transaction.add(R.id.view_container, fragment);
                transaction.addToBackStack("");
                transaction.commit();
            }
        });
        CommonUtil.setTvBoldFace(mTvTitle);
        showDialog();
        NewCloudDataUtil.getBillBoard(ActionHotSongs.TYPE_HOME, Config.FROM);
        NewCloudDataUtil.getNewSongs(ActionNewSongs.TYPE_HOME, Config.FROM, 0, 7);
        initTemp();
    }

    private void initRefreshLayout() {
        refreshLayout.setSize(SwipeRefreshLayout.LARGE);
        refreshLayout.setProgressViewOffset(true, -0, 100);
        refreshLayout.setProgressViewEndTarget(true, 180);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark));
        refreshLayout.setDistanceToTriggerSync(200);
        refreshLayout.setOnChildScrollUpCallback(null);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NewCloudDataUtil.getBillBoard(ActionHotSongs.TYPE_HOME, Config.FROM);
                NewCloudDataUtil.getNewSongs(ActionNewSongs.TYPE_HOME, Config.FROM, 0, 7);

            }
        });

    }

    @OnClick(R.id.tv_hot_more)
    void onClickHotMore() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        HotNewListFragment.TYPE = HotNewListFragment.TYPE_HOT;
        Fragment fragment = new HotNewListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", Config.FROM);
        bundle.putString("rankId", Config.FROM == Config.FROM_JAPAN ? BillBoardMusicListRequest.RANK_Japan_TOP
                : BillBoardMusicListRequest.RANK_Europe_US);
        if (mHotList != null && mHotList.size() > 0) {
            bundle.putSerializable("songs", mHotList);
        }
        fragment.setArguments(bundle);
        transaction.add(R.id.view_container, fragment);
        transaction.addToBackStack("");
        transaction.commit();
    }

    @OnClick(R.id.tv_new_more)
    void onClickNewMore() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        HotNewListFragment.TYPE = HotNewListFragment.TYPE_NEW;
        Fragment fragment = new HotNewListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", Config.FROM);
        if (mNewList != null && mNewList.size() > 0) {
            bundle.putSerializable("songs", mNewList);
        }
        fragment.setArguments(bundle);
        transaction.add(R.id.view_container, fragment);
        transaction.addToBackStack("");
        transaction.commit();
    }


    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPosting(ActionNewSongs event) {
        closeDialog();
        refreshLayout.setRefreshing(false);
        if (event.type == ActionNewSongs.TYPE_HOME) {
            if (event != null && event.trackList != null && event.trackList.size() >= 6) {
                saveTemp(event.trackList, SavedStateBean.TAG_NEW);
                new0.refreshView(event.trackList.get(0));
                new1.refreshView(event.trackList.get(1));
                new2.refreshView(event.trackList.get(2));
                new3.refreshView(event.trackList.get(3));
                new4.refreshView(event.trackList.get(4));
                new5.refreshView(event.trackList.get(5));
            } else {
                ToastUtils.show(R.string.please_check_net);
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPosting(ActionHotSongs event) {
        closeDialog();
        refreshLayout.setRefreshing(false);
        if (event.type == ActionHotSongs.TYPE_HOME) {
            if (event != null && event.trackList != null && event.trackList.size() >= 6) {
                saveTemp(event.trackList, SavedStateBean.TAG_HOT);
                List<Song> subList = event.trackList.subList(0, 6);
                hot0.refreshView(subList.get(0));
                hot1.refreshView(subList.get(1));
                hot2.refreshView(subList.get(2));
                hot3.refreshView(subList.get(3));
                hot4.refreshView(subList.get(4));
                hot5.refreshView(subList.get(5));
//
            } else {
                ToastUtils.show(R.string.please_check_net);
            }
        }
    }

    private void initTemp() {
        getSavedTemp(SavedStateBean.TAG_HOT, new OnTempGetListener() {
            @Override
            public void onGet(List<Song> songList) {
                closeDialog();
                hot0.refreshView(songList.get(0));
                hot1.refreshView(songList.get(1));
                hot2.refreshView(songList.get(2));
                hot3.refreshView(songList.get(3));
                hot4.refreshView(songList.get(4));
                hot5.refreshView(songList.get(5));
            }

            @Override
            public void onGetFail() {

            }
        });

        getSavedTemp(SavedStateBean.TAG_NEW, new OnTempGetListener() {
            @Override
            public void onGet(List<Song> songList) {
                closeDialog();
                new0.refreshView(songList.get(0));
                new1.refreshView(songList.get(1));
                new2.refreshView(songList.get(2));
                new3.refreshView(songList.get(3));
                new4.refreshView(songList.get(4));
                new5.refreshView(songList.get(5));
            }

            @Override
            public void onGetFail() {

            }
        });
    }


    private void saveTemp(List<Song> songList, String tag) {
        SavedStateBean currentStateBean = new SavedStateBean();
        currentStateBean.setTag(tag);
        currentStateBean.setCurrentPlayList(new Gson().toJson(songList));
        currentStateBean.saveOrUpdate("tag = ?", tag);
    }

    private void getSavedTemp(String tag, final OnTempGetListener onTempGetListener) {
        LitePal.where("tag = ?", tag).findFirstAsync(SavedStateBean.class).listen(new FindCallback<SavedStateBean>() {
            @Override
            public void onFinish(SavedStateBean savedStateBean) {
                if (savedStateBean != null) {
                    List<Song> songList = new Gson().fromJson(savedStateBean.getCurrentPlayList(), new TypeToken<List<Song>>() {
                    }.getType());
                    if (onTempGetListener != null) {
                        if (songList != null && songList.size() > 0) {
                            onTempGetListener.onGet(songList);
                        } else {
                            onTempGetListener.onGetFail();
                        }
                    }

                }
            }
        });

    }

    public interface OnTempGetListener {
        void onGet(List<Song> songList);

        void onGetFail();
    }
}
