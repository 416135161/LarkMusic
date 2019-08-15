package internet.com.larkmusic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionMainBottomMenu;
import internet.com.larkmusic.fragment.AlbumsListFragment;

/**
 * Created by sjning
 * created on: 2019/5/13 下午10:43
 * description:
 */
public class GenreView extends FrameLayout {
    Drawable drawable;
    int srcId;
    String name;
    String from;

    public GenreView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public GenreView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        inflate(context, R.layout.view_genre, this);
        TextView mTvName = findViewById(R.id.tv_name);
        ImageView mIvPhoto = findViewById(R.id.iv_photo);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GenreView, 0, 0);
        drawable = a.getDrawable(R.styleable.GenreView_src);
        srcId = a.getResourceId(R.styleable.GenreView_src, R.mipmap.ic_song_default);
        name = a.getString(R.styleable.GenreView_name);
        from = a.getString(R.styleable.GenreView_from);
        mTvName.setText(name);
        mIvPhoto.setImageDrawable(drawable);
    }

    public void onClick(FragmentTransaction transaction) {
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        Fragment fragment = new AlbumsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        bundle.putString("name", name);
        bundle.putInt("srcId", srcId);
        fragment.setArguments(bundle);
        transaction.add(R.id.view_container, fragment);
        transaction.addToBackStack("");
        transaction.commit();
        EventBus.getDefault().post(new ActionMainBottomMenu(false));
    }

}

