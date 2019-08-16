package internet.com.larkmusic.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionDownLoad;
import internet.com.larkmusic.app.MusicApplication;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.network.GetSongCallBack;
import internet.com.larkmusic.player.MusicPlayer;
import internet.com.larkmusic.util.CloudDataUtil;
import internet.com.larkmusic.util.CommonUtil;
import internet.com.larkmusic.util.FavoriteService;
import internet.com.larkmusic.util.FileUtils;

/**
 * Created by sjning
 * created on: 2019/6/12 下午5:23
 * description:
 */
public class OperateDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    Song song;

    boolean showDelete;
    boolean showDownload = true;
    OnDeleteListener onDeleteListener;

    public OperateDialog() {
    }


    public OperateDialog setSong(Song song) {
        this.song = song;
        return this;
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
        TextView tvDownload = rootView.findViewById(R.id.tv_download);
        tvDownload.setOnClickListener(this);
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
        rootView.findViewById(R.id.tv_playlist).setOnClickListener(this);
        if (showDelete) {
            tvDelete.setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.tv_playlist).setVisibility(View.GONE);
        }
        if(!showDownload){
            tvDownload.setVisibility(View.GONE);
        }
        if (song != null) {
            tvSong.setText(song.getSongName());
            tvSinger.setText(song.getSingerName());
            if(!TextUtils.isEmpty(song.getImgUrl())){
                Picasso.with(getContext())
                        .load(song.getImgUrl())
                        .error(R.mipmap.ic_singer_default)
                        .placeholder(R.mipmap.ic_singer_default)
                        .into(ivSinger);
            }else {
                ivSinger.setImageResource(R.mipmap.ic_singer_default);
            }
        }

    }


    @Override
    public void onClick(View view) {
        if(!CommonUtil.isNotFastClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.tv_download:
                // 先判断是否已经下载，如果已经下载则直接关闭，如果没有下载则去请求下载地址下载
                String downloadPath = CommonUtil.getSongSavePath(song.getHash());
                if (!FileUtils.isFileExist(downloadPath)) {
                    CloudDataUtil.getSongPlayUrl(song, new GetSongCallBack() {
                        @Override
                        public void onSongGetOk(Song song) {
                            EventBus.getDefault().post(new ActionDownLoad(song));
                            dismiss();
                        }

                        @Override
                        public void onSongGetFail() {
                            Toast.makeText(MusicApplication.getInstance(), MusicApplication.getInstance().getString(R.string.can_not_download), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    dismiss();
                }
                break;
            case R.id.tv_next:
                MusicPlayer.getPlayer().addQueueNext(song);
                dismiss();
                break;
            case R.id.tv_later:
                MusicPlayer.getPlayer().addQueueLater(song);
                dismiss();
                break;
            case R.id.tv_add_list:
                FavoriteService.getInstance().saveSong(song, true);
                dismiss();
                break;
            case R.id.tv_delete:
                if (onDeleteListener != null) {
                    onDeleteListener.onDelete(song);
                }
                dismiss();
                break;
            case R.id.tv_playlist:
                new PlayListDialog().setSong(song).show(getFragmentManager(), PlayListDialog.class.getName());
                dismiss();
                break;
            case R.id.tv_close:
                dismiss();
                break;
        }

    }

    public OperateDialog setShowDelete(boolean showDelete) {
        this.showDelete = showDelete;
        return this;
    }
    public OperateDialog setShowDownload(boolean showDownload) {
        this.showDownload = showDownload;
        return this;
    }


    public OperateDialog setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
        return this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public interface OnDeleteListener {
        void onDelete(Song song);
    }

}
