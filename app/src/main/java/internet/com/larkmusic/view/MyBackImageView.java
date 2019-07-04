package internet.com.larkmusic.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import internet.com.larkmusic.action.ActionBack;

/**
 * Created by sjning
 * created on: 2019-07-04 17:51
 * description:
 */
public class MyBackImageView extends AppCompatImageView implements View.OnClickListener {
    public MyBackImageView(Context context) {
        super(context);
        this.setOnClickListener(this);
    }

    public MyBackImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setOnClickListener(this);
    }

    public MyBackImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        EventBus.getDefault().post(new ActionBack());
    }
}
