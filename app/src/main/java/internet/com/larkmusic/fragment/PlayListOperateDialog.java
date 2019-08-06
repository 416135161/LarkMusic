package internet.com.larkmusic.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionRefreshPlayList;
import internet.com.larkmusic.bean.PlayListBean;
import internet.com.larkmusic.bean.PlayListRelationBean;
import internet.com.larkmusic.util.ToastUtils;

/**
 * Created by sjning
 * created on: 2019-08-06 14:58
 * description:
 */
public class PlayListOperateDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    PlayListBean playListBean;
    OnDeleteListener onDeleteListener;
    AlertDialog editDialog;

    public PlayListOperateDialog() {
    }


    public PlayListOperateDialog setSong(PlayListBean playListBean) {
        this.playListBean = playListBean;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.layout_play_list_operate_dialog, null);
        initView(view);
        dialog.setContentView(view);
        ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        return dialog;
    }

    private void initView(View rootView) {
        TextView tvName = rootView.findViewById(R.id.tv_name);

        TextView tvEdit = rootView.findViewById(R.id.tv_edit);
        tvEdit.setOnClickListener(this);
        TextView tvDelete = rootView.findViewById(R.id.tv_delete);
        tvDelete.setOnClickListener(this);
        TextView tvClose = rootView.findViewById(R.id.tv_close);
        tvClose.setOnClickListener(this);
        if (playListBean != null) {
            tvName.setText(playListBean.getName());
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_delete:
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.play_list_delete_tip))
                        .setPositiveButton(getString(R.string.text_sure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                playListBean.delete();
                                LitePal.deleteAll(PlayListRelationBean.class, "playListName = ? ", playListBean.getName());
                                EventBus.getDefault().post(new ActionRefreshPlayList());
                                dismiss();
                            }
                        })
                        .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
            case R.id.tv_edit:
                View contentView = getLayoutInflater().inflate(R.layout.dlg_add_palylist, null);
                final EditText editText = contentView.findViewById(R.id.et_name);
                editText.setText(playListBean.getName());
                editDialog = new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.add_play_list))
                        .setView(contentView)
                        .setPositiveButton(getString(R.string.text_sure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!TextUtils.isEmpty(editText.getText().toString())) {
                                    String originalName = playListBean.getName();
                                    playListBean.setName(editText.getText().toString());
                                    //如果已存在此歌单，则直接合并
                                    if (LitePal.isExist(PlayListBean.class, "name = ?", editText.getText().toString())) {
                                        playListBean.delete();
                                        ContentValues values = new ContentValues();
                                        if (!TextUtils.isEmpty(playListBean.getIcon())) {
                                            values.put("icon", playListBean.getIcon());
                                            LitePal.updateAll(PlayListBean.class, values, "name = ? ", playListBean.getName());
                                        }
                                    } else { // 不存在此歌单则变更歌单名称
                                        playListBean.saveOrUpdate("name = ?", originalName);
                                    }
                                    //更新歌单和歌曲的关系表
                                    ContentValues values = new ContentValues();
                                    values.put("playListName", playListBean.getName());
                                    LitePal.updateAll(PlayListRelationBean.class, values, "playListName = ? ", originalName);
                                    EventBus.getDefault().post(new ActionRefreshPlayList());
                                    dismiss();
                                } else {
                                    editDialog.show();
                                    ToastUtils.show(R.string.add_play_list_empty_tip);
                                }
                            }
                        })
                        .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                editDialog.show();
                break;
            case R.id.tv_close:
                dismiss();
                break;
        }

    }

    public PlayListOperateDialog setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
        return this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public interface OnDeleteListener {
        void onDelete(PlayListBean playListBean);
    }

}
