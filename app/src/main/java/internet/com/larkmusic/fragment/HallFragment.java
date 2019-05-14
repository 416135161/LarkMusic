package internet.com.larkmusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
import internet.com.larkmusic.network.Config;
import internet.com.larkmusic.util.CloudDataUtil;
import internet.com.larkmusic.util.CommonUtil;
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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hall;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = new ArrayList<>();                  //ImageView数据
        mList.add(R.mipmap.banner_1);
        mList.add(R.mipmap.banner_2);
        mList.add(R.mipmap.banner_3);

        mTitles = new ArrayList<>();                //title数据
        mTitles.add("第一页到货发动你的号看到回复");
        mTitles.add("第二页佛挡");
        mTitles.add("第三页大佛我as的后if好的搜iuuuu家的三");

        mBannerView.setImages(mList)        //ImageView数据
                .setIndcatorTitles(mTitles) //title数据
                .start();
        CommonUtil.setTvBoldFace(mTvTitle);
        showDialog();
        CloudDataUtil.getHotSongs(ActionHotSongs.TYPE_HOME, Config.FROM);
        CloudDataUtil.getNewSongs(ActionNewSongs.TYPE_HOME, Config.FROM);

    }

    @OnClick(R.id.tv_hot_more)
    void onClickHotMore() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        HotNewListFragment.TYPE = HotNewListFragment.TYPE_HOT;
        Fragment fragment = new HotNewListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", Config.FROM_US);
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
        bundle.putString("from", Config.FROM_US);
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
        if (event.type == ActionNewSongs.TYPE_HOME) {
            if (event != null && event.trackList != null && event.trackList.size() >= 6) {
                new0.refreshView(event.trackList.get(0));
                new1.refreshView(event.trackList.get(1));
                new2.refreshView(event.trackList.get(2));
                new3.refreshView(event.trackList.get(3));
                new4.refreshView(event.trackList.get(4));
                new5.refreshView(event.trackList.get(5));
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPosting(ActionHotSongs event) {
        closeDialog();
        if (event.type == ActionHotSongs.TYPE_HOME) {
            if (event != null && event.trackList != null && event.trackList.size() >= 6) {
                hot0.refreshView(event.trackList.get(0));
                hot1.refreshView(event.trackList.get(1));
                hot2.refreshView(event.trackList.get(2));
                hot3.refreshView(event.trackList.get(3));
                hot4.refreshView(event.trackList.get(4));
                hot5.refreshView(event.trackList.get(5));
            }
        }

    }
}
