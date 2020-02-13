package internet.com.larkmusic.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.SpriteFactory;
import com.github.ybq.android.spinkit.Style;
import com.github.ybq.android.spinkit.sprite.Sprite;

import internet.com.larkmusic.R;

/**
 * Created by sjning
 * created on: 2019/5/13 下午4:47
 * description:
 */
public class WaitDialog extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MyMiddleDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); //无标题
        View view = inflater.inflate(R.layout.net_wait_dialog, container);
        SpinKitView spinKitView = view.findViewById(R.id.spin_kit);
        Sprite drawable = SpriteFactory.create(Style.CIRCLE);
        spinKitView.setIndeterminateDrawable(drawable);
        return view;
    }
}
