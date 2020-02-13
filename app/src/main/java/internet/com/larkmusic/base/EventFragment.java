package internet.com.larkmusic.base;

import android.os.Bundle;
import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by sjning
 * created on: 2019/5/12 下午11:27
 * description:
 */
public abstract class EventFragment extends BaseFragment{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
