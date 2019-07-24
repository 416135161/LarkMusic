package internet.com.larkmusic.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionSelectSong;
import internet.com.larkmusic.adapter.PlayingListAdapter;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.player.MusicPlayer;

/**
 * Created by sjning
 * created on: 2019-07-04 16:10
 * description:正在播放中的歌曲
 */
public class PlayingListDialog extends BottomSheetDialogFragment {

    ListView mListView;
    PlayingListAdapter mAdapter;

    public PlayingListDialog() {
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.layout_playing_list, null);
        initView(view);
        dialog.setContentView(view);

        try {
            Field mBehaviorField = dialog.getClass().getDeclaredField("behavior");
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

    private void initView(View rootView) {
        mListView = rootView.findViewById(R.id.rv_list);
        mAdapter = new PlayingListAdapter(getContext(), MusicPlayer.getPlayer().getQueue());
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventBus.getDefault().post(new ActionSelectSong((Song) mAdapter.getItem(i)));
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });
    }


}