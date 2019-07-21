package internet.com.larkmusic.fragment;

/**
 * Created by sjning
 * created on: 2019-07-20 08:52
 * description:
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import internet.com.larkmusic.R;
import internet.com.larkmusic.adapter.PlayListAdapter;
import internet.com.larkmusic.bean.PlayListBean;
import internet.com.larkmusic.bean.PlayListRelationBean;
import internet.com.larkmusic.bean.Song;

/**
 * 歌单列表弹出框
 */
public class PlayListDialog extends BottomSheetDialogFragment {

    ListView mListView;
    PlayListAdapter mAdapter;
    Song song;

    public PlayListDialog() {
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.layout_play_list, null);
        initView(view);
        dialog.setContentView(view);

        try {
            Field mBehaviorField = dialog.getClass().getDeclaredField("mBehavior");
            mBehaviorField.setAccessible(true);
            final BottomSheetBehavior behavior = (BottomSheetBehavior) mBehaviorField.get(dialog);
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        return dialog;
    }


    public PlayListDialog setSong(Song song) {
        this.song = song;
        return this;
    }

    private void initView(View rootView) {
        mListView = rootView.findViewById(R.id.rv_list);
        mAdapter = new PlayListAdapter(getContext(), null);

        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (song != null && mAdapter.getItem(i) != null) {
                    PlayListBean playListBean = (PlayListBean) mAdapter.getItem(i);
                    PlayListRelationBean playListRelationBean = new PlayListRelationBean();
                    playListRelationBean.setPlayListName(playListBean.getName());
                    playListRelationBean.setSongHash(song.getHash());
                    playListRelationBean.saveOrUpdate("playListName = ? AND songHash = ?",
                            playListRelationBean.getPlayListName(), playListRelationBean.getSongHash());

                    playListBean.setIcon(song.getImgUrl());
                    playListBean.saveOrUpdate("name = ?", playListBean.getName());
                    song.saveOrUpdate("hash = ?", song.getHash());
                    dismiss();
                }
            }
        });
        refreshView();

        rootView.findViewById(R.id.tv_add_play_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View contentView = getLayoutInflater().inflate(R.layout.dlg_add_palylist, null);
                final EditText editText = contentView.findViewById(R.id.et_name);
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.add_play_list))
                        .setView(contentView)
                        .setPositiveButton(getString(R.string.text_sure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!TextUtils.isEmpty(editText.getText().toString())) {
                                    PlayListBean playListBean = new PlayListBean();
                                    playListBean.setName(editText.getText().toString());
                                    playListBean.saveOrUpdate("name = ?", playListBean.getName());
                                    refreshView();
                                } else {
                                    Toast.makeText(getContext(), R.string.add_play_list_emputy_tip, Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

    }

    private void refreshView() {
        LitePal.findAllAsync(PlayListBean.class).listen(new FindMultiCallback<PlayListBean>() {
            @Override
            public void onFinish(List<PlayListBean> list) {
                Collections.reverse(list);
                mAdapter.setPlayList(list);
                mAdapter.notifyDataSetChanged();
            }
        });
    }


}