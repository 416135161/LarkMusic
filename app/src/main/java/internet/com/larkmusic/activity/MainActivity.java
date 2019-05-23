package internet.com.larkmusic.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import internet.com.larkmusic.R;
import internet.com.larkmusic.back.BackHandlerHelper;
import internet.com.larkmusic.fragment.HallFragment;
import internet.com.larkmusic.fragment.LibraryFragment;
import internet.com.larkmusic.fragment.MeFragment;
import internet.com.larkmusic.fragment.SearchFragment;
import internet.com.larkmusic.network.Config;

public class MainActivity extends AppCompatActivity {

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
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        onClickViewHall();

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
    }

    @OnClick(R.id.view_library)
    void onClickViewLibrary() {
        setAllNormal();
        ivLibrary.setImageResource(R.mipmap.tab_library_select);
        tvLibrary.setTextColor(getResources().getColor(R.color.text_red));
        startFragment(LibraryFragment.class, new Bundle());
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
            super.onBackPressed();
        }
    }

}
