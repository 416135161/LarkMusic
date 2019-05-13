package internet.com.larkmusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import internet.com.larkmusic.R;
import internet.com.larkmusic.back.BackHandlerHelper;
import internet.com.larkmusic.back.FragmentBackHandler;
import internet.com.larkmusic.base.BaseFragment;
import internet.com.larkmusic.network.Config;

/**
 * Created by sjning
 * created on: 2019/5/6 上午11:03
 * description:
 */
public class HallFragment extends BaseFragment implements FragmentBackHandler {
    List<Integer> mList;
    List<String> mTitles;

    @BindView(R.id.banner_bv)
    com.alin.lib.bannerlib.BannerView mBannerView;

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
}
