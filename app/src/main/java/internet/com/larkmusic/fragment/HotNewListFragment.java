package internet.com.larkmusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionHotSongs;
import internet.com.larkmusic.action.ActionNewSongs;
import internet.com.larkmusic.action.ActionSelectSong;
import internet.com.larkmusic.adapter.HotNewListAdapter;
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
    ListView mRvSongs;
    private HotNewListAdapter mAdapter;


    TextView mTvTitle;
    TextView mTvCount;
    ImageView mIvHeader;


    String from;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot_new_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        from = getArguments().getString("from", Config.FROM_US);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHeaderAndFooter();
        initView();
        if (TYPE == TYPE_NEW) {
            mIvHeader.setImageResource(R.mipmap.ic_new_header_bg);
        } else {
            mIvHeader.setImageResource(R.mipmap.ic_hot_header_bg);
        }

        if (getArguments().getSerializable("songs") != null) {
            ArrayList<Song> songs = (ArrayList<Song>) getArguments().getSerializable("songs");
            mAdapter.setPlayList(songs);
            mAdapter.notifyDataSetChanged();
            mTvCount.setText(String.format(getString(R.string.title_song_count), mAdapter.getCount()));
        } else {
            showDialog();
            if (TYPE == TYPE_NEW) {
                CloudDataUtil.getNewSongs(ActionNewSongs.TYPE_LIST, from);
            } else {
                CloudDataUtil.getHotSongs(ActionHotSongs.TYPE_LIST, from);
            }
        }
    }

    private void initView() {
        mAdapter = new HotNewListAdapter(getContext(), new ArrayList<Song>());
        mRvSongs.setAdapter(mAdapter);
        mRvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                }
                i -= 1;
                EventBus.getDefault().post(new ActionSelectSong((Song) mAdapter.getItem(i)));
            }
        });
        if (TYPE == TYPE_HOT) {
            mTvTitle.setText(R.string.title_hot);
        } else if (TYPE == TYPE_NEW) {
            mTvTitle.setText(R.string.title_new);
        }
    }

    private void initHeaderAndFooter() {
        View header = getLayoutInflater().inflate(R.layout.layout_hot_new_list_header, null);
        mRvSongs.addHeaderView(header);
        mTvCount = header.findViewById(R.id.tv_count);
        mTvTitle = header.findViewById(R.id.tv_title);
        mIvHeader = header.findViewById(R.id.iv_header);
        header.findViewById(R.id.view_play_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAdapter.getCount() > 0) {
                    EventBus.getDefault().post(new ActionSelectSong(mAdapter.getSongs()));
                }
            }
        });

        View footer = new View(getContext());
        footer.setMinimumHeight(50);
        mRvSongs.addFooterView(footer);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPosting(ActionNewSongs event) {
        closeDialog();
        if (event.type == ActionNewSongs.TYPE_LIST && event.from == from) {
            if (event != null && event.trackList != null && event.trackList.size() > 0) {
                mAdapter.setPlayList(event.trackList);
                mAdapter.notifyDataSetChanged();
            }
        }
        mTvCount.setText(String.format(getString(R.string.title_song_count), mAdapter.getCount()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPosting(ActionHotSongs event) {
        closeDialog();
        if (event.type == ActionHotSongs.TYPE_LIST && event.from == from) {
            if (event != null && event.trackList != null && event.trackList.size() > 0) {
                mAdapter.setPlayList(event.trackList);
                mAdapter.notifyDataSetChanged();
            }
        }
        mTvCount.setText(String.format(getString(R.string.title_song_count), mAdapter.getCount()));

    }
}
