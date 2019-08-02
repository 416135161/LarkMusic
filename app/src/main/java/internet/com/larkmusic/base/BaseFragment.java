package internet.com.larkmusic.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import internet.com.larkmusic.R;
import internet.com.larkmusic.fragment.WaitDialog;

/**
 * Created by sjning
 * created on: 2019/5/12 下午9:51
 * description:
 */
public abstract class BaseFragment extends Fragment {
    WaitDialog customLoseDialog;
    Unbinder bind;

    @BindView(R.id.iv_refresh)
    ImageView mIvRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base, null);
        View view = inflater.inflate(getLayoutId(), null);
        ((FrameLayout) rootView.findViewById(R.id.view_root_container)).addView(view);
        bind = ButterKnife.bind(this, rootView);
        return rootView;

    }

    protected abstract int getLayoutId();

    @OnClick(R.id.iv_refresh)
    void onClickRefresh() {
        onRefresh();
    }

    protected void onRefresh() {

    }

    protected void showRefresh(){
        mIvRefresh.setVisibility(View.VISIBLE);
    }

    protected void hideRefresh(){
        mIvRefresh.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除绑定
        bind.unbind();
    }

    protected void showDialog() {
        if (customLoseDialog == null) {
            customLoseDialog = new WaitDialog();
        }
        customLoseDialog.show(getFragmentManager(), "lose");
    }

    protected void closeDialog() {
        if (customLoseDialog != null && !customLoseDialog.isHidden()) {
            customLoseDialog.dismiss();
        }
    }

}
