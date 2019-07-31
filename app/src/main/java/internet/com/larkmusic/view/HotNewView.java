package internet.com.larkmusic.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionSelectSong;
import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2019/5/6 下午2:46
 * description:
 */
public class HotNewView extends FrameLayout {

    TextView mTvSongName;
    TextView mTvSingerName;
    ImageView mIvPhoto;

    public HotNewView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public HotNewView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HotNewView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.view_hot_new_item, this);
        mTvSingerName = findViewById(R.id.tv_singer_name);
        mTvSongName = findViewById(R.id.tv_song_name);
        mIvPhoto = findViewById(R.id.iv_photo);
    }

    public void refreshView(final Song song) {
        mTvSongName.setText(song.getSongName());
        mTvSingerName.setText(song.getSingerName());
        try {
            String imgUrl = "";
            if(!TextUtils.isEmpty(song.getImgUrl())){
                imgUrl = song.getImgUrl().replace("90x90", "150x150");
            }
            Picasso.with(getContext())
                    .load(imgUrl)
                    .error(R.mipmap.ic_song_default)
                    .placeholder(R.mipmap.ic_song_default)
                    .into(mIvPhoto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                EventBus.getDefault().post(new ActionSelectSong(song));

            }
        });
    }

}
