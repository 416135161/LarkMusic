package internet.com.larkmusic.listener;

import android.view.View;

import org.greenrobot.eventbus.EventBus;

import internet.com.larkmusic.action.ActionShowOperateDlg;
import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2019/6/13 上午11:22
 * description:
 */
public class MyClickListener implements View.OnClickListener {

    private Song song;

    public MyClickListener(Song song) {
        this.song = song;
    }

    @Override
    public void onClick(View view) {
        EventBus.getDefault().post(new ActionShowOperateDlg(song));
    }
}
