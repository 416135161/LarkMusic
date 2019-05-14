package internet.com.larkmusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionHotSongs;
import internet.com.larkmusic.action.ActionNewSongs;
import internet.com.larkmusic.adapter.HotNewListAdapter;
import internet.com.larkmusic.base.BaseFragment;
import internet.com.larkmusic.base.EventFragment;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.network.Config;
import internet.com.larkmusic.util.CloudDataUtil;

/**
 * Created by sjning
 * created on: 2019/5/6 下午3:49
 * description:
 */
public class HotNewListFragment extends EventFragment {
    public static int TYPE;
    public static int TYPE_HOT = 0;
    public static int TYPE_NEW = 1;

    @BindView(R.id.rv_songs)
    RecyclerView mRvSongs;
    private HotNewListAdapter mAdapter;

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_count)
    TextView mTvCount;

    String from;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot_new_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (TYPE == TYPE_HOT) {
            mTvTitle.setText(R.string.title_hot);
        } else if (TYPE == TYPE_NEW) {
            mTvTitle.setText(R.string.title_new);
        }
        from = getArguments().getString("from", Config.FROM_US);
        mAdapter = new HotNewListAdapter(getContext(), new ArrayList<Song>());
        mAdapter.setOnItemClickListener(new HotNewListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mAdapter.setSelectedPosition(position);
                mAdapter.notifyDataSetChanged();
            }
        });
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRvSongs.setLayoutManager(mLayoutManager2);
        mRvSongs.setItemAnimator(new DefaultItemAnimator());
        mRvSongs.setAdapter(mAdapter);
        showDialog();
        if (TYPE == TYPE_NEW) {
            CloudDataUtil.getNewSongs(ActionNewSongs.TYPE_LIST, Config.FROM);
        } else {
            CloudDataUtil.getHotSongs(ActionHotSongs.TYPE_LIST, Config.FROM);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPosting(ActionNewSongs event) {
        closeDialog();
        if (event.type == ActionNewSongs.TYPE_LIST && event.from == from) {
            if (event != null && event.trackList != null && event.trackList.size() > 0) {
                mAdapter.setPlaylists(event.trackList);
                mAdapter.notifyDataSetChanged();
            }
        }
        mTvCount.setText(String.format(getString(R.string.title_song_count), mAdapter.getItemCount()));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPosting(ActionHotSongs event) {
        closeDialog();
        if (event.type == ActionHotSongs.TYPE_LIST && event.from == from) {
            if (event != null && event.trackList != null && event.trackList.size() > 0) {
                mAdapter.setPlaylists(event.trackList);
                mAdapter.notifyDataSetChanged();
            }
        }
        mTvCount.setText(String.format(getString(R.string.title_song_count), mAdapter.getItemCount()));

    }
}
