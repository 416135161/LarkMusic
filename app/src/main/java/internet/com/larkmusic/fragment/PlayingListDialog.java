package internet.com.larkmusic.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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
import internet.com.larkmusic.util.CommonUtil;

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
                if(!CommonUtil.isNotFastClick()){
                    return;
                }
                EventBus.getDefault().post(new ActionSelectSong((Song) mAdapter.getItem(i)));
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 1000);
            }
        });

        View footer = new View(getContext());
        footer.setMinimumHeight(50);
        mListView.addFooterView(footer);
    }


}