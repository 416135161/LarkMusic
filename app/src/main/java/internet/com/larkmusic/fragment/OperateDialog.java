package internet.com.larkmusic.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import internet.com.larkmusic.R;
import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2019/6/12 下午5:23
 * description:
 */
public class OperateDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    Song song;

    public OperateDialog() {
    }

    public void setSong(Song song) {
        this.song = song;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.layout_operate_dialog, null);
        initView(view);
        dialog.setContentView(view);
        ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        return dialog;
    }

    private void initView(View rootView) {
        ImageView ivSinger = rootView.findViewById(R.id.iv_singer);
        TextView tvSong = rootView.findViewById(R.id.tv_song);
        TextView tvSinger = rootView.findViewById(R.id.tv_singer);
        TextView tvNext = rootView.findViewById(R.id.tv_next);
        tvNext.setOnClickListener(this);
        TextView tvLater = rootView.findViewById(R.id.tv_later);
        tvLater.setOnClickListener(this);
        TextView tvAddList = rootView.findViewById(R.id.tv_add_list);
        tvAddList.setOnClickListener(this);
        TextView tvDelete = rootView.findViewById(R.id.tv_delete);
        tvDelete.setOnClickListener(this);
        TextView tvClose = rootView.findViewById(R.id.tv_close);
        tvClose.setOnClickListener(this);

        if (song != null) {
            tvSong.setText(song.getSongName());
            tvSinger.setText(song.getSingerName());
            ivSinger.setImageResource(R.mipmap.ic_singer_default);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
                break;
            case R.id.tv_later:
                break;
            case R.id.tv_add_list:
                break;
            case R.id.tv_delete:
                break;
            case R.id.tv_cancel:
                break;
        }
        dismiss();
    }
}
