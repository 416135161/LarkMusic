package internet.com.larkmusic.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;

import butterknife.OnClick;
import internet.com.larkmusic.R;
import internet.com.larkmusic.back.BackHandlerHelper;
import internet.com.larkmusic.back.FragmentBackHandler;
import internet.com.larkmusic.base.BaseFragment;
import internet.com.larkmusic.util.CommonUtil;
import internet.com.larkmusic.view.GenreListItemView;
import internet.com.larkmusic.view.GenreView;

/**
 * Created by sjning
 * created on: 2019/5/7 下午8:17
 * description:
 */
public class LibraryFragment extends BaseFragment implements FragmentBackHandler {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_library;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        view.findViewById()
    }


    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
    }

    @OnClick({R.id.topUS, R.id.topJapan, R.id.topCountry,
            R.id.topPop, R.id.topElectronic, R.id.topRock})
    void onClickTop(View view) {
        if(!CommonUtil.isNotFastClick()){
            return;
        }
        ((GenreView) view).onClick(getChildFragmentManager().beginTransaction());
    }

    @OnClick({R.id.b_us, R.id.b_japan, R.id.b_country,
            R.id.b_pop, R.id.b_electronic, R.id.b_rock})
    void onClickBottom(View view) {
        if(!CommonUtil.isNotFastClick()){
            return;
        }
        ((GenreListItemView) view).onClick(getChildFragmentManager().beginTransaction());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

}
