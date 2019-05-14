package internet.com.larkmusic.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import internet.com.larkmusic.R;

/**
 * Created by sjning
 * created on: 2019/5/13 下午11:20
 * description:
 */
public class GenreListItemView extends FrameLayout {

    TextView mTvName;
    ImageView mIvPhoto;

    public GenreListItemView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public GenreListItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public GenreListItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.view_genre_list_item, this);
        mTvName = findViewById(R.id.tv_name);
        mIvPhoto = findViewById(R.id.iv_photo);
    }

    public void refreshView(String title, String imgUrl) {
        mTvName.setText(title);
        try {
            Picasso.with(getContext())
                    .load(imgUrl)
                    .error(R.mipmap.ic_default)
                    .placeholder(R.mipmap.ic_default)
                    .into(mIvPhoto);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}