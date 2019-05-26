package internet.com.larkmusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;


import butterknife.ButterKnife;
import internet.com.larkmusic.R;
import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2019/5/26 下午2:58
 * description:
 */
public class PlayerActivity extends AppCompatActivity {
    public static final String INTENT_KEY_SONG = "intent_key_song";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
    }

    public static void start(Song song, Activity activity) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_KEY_SONG, song);
        intent.setClass(activity, PlayerActivity.class);
        activity.startActivity(intent);
    }
}
