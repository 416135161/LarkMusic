package internet.com.larkmusic.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;

import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionSelectSong;
import internet.com.larkmusic.adapter.PlayingListAdapter;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.player.MusicPlayer;

/**
 * Created by sjning
 * created on: 2019-07-04 16:10
 * description:
 */
public class PlayingListDialog extends BottomSheetDialogFragment {

    ListView mListView;
    PlayingListAdapter mAdapter;

    public PlayingListDialog() {
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.layout_playing_list, null);
        initView(view);
        dialog.setContentView(view);
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