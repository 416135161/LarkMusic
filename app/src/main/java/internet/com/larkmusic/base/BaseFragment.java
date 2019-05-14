package internet.com.larkmusic.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import internet.com.larkmusic.fragment.WaitDialog;

/**
 * Created by sjning
 * created on: 2019/5/12 下午9:51
 * description:
 */
public abstract class BaseFragment extends Fragment {
    WaitDialog customLoseDialog;
    Unbinder bind;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), null);
        bind = ButterKnife.bind(this, view);
        return view;

    }

    protected abstract int getLayoutId();

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
