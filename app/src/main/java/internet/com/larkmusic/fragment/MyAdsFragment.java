package internet.com.larkmusic.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import internet.com.larkmusic.R;
import internet.com.larkmusic.network.netnew.NewApi;
import internet.com.larkmusic.network.netnew.bean.GetAdsResponse;

/**
 * Created by sjning
 * created on: 2019-08-07 14:34
 * description:
 */
public class MyAdsFragment extends DialogFragment {
    ImageView ivIcon;
    private GetAdsResponse.ResultBean resultBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_my_ads, container, false);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ivIcon = view.findViewById(R.id.iv_icon);
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(resultBean.url);//此处填链接
                intent.setData(content_url);
                startActivity(intent);
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        window.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.color.transparent));

        Picasso.with(getContext())
                .load(NewApi.HOST + resultBean.icon)
                .error(R.mipmap.ic_song_default)
                .placeholder(R.mipmap.ic_song_default)
                .into(ivIcon);
    }

    public MyAdsFragment setResultBean(GetAdsResponse.ResultBean resultBean) {
        this.resultBean = resultBean;
        return this;

    }

    public static MyAdsFragment newInstance() {
        Bundle args = new Bundle();
        MyAdsFragment fragment = new MyAdsFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
