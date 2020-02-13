package internet.com.larkmusic.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionMainBottomMenu;
import internet.com.larkmusic.action.ActionSelectSong;
import internet.com.larkmusic.adapter.LocalListAdapter;
import internet.com.larkmusic.base.BaseFragment;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.util.AudioUtils;
import internet.com.larkmusic.util.CommonUtil;

/**
 * Created by sjning
 * created on: 2019-07-12 22:14
 * description:
 */
public class LocalSongsFragment extends BaseFragment {


    @BindView(R.id.rv_songs)
    ListView mRvSongs;
    private LocalListAdapter mAdapter;


    TextView mTvTitle;
    TextView mTvCount;
    ImageView mIvHeader;
    ImageView mIvIcon;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_local_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new ActionMainBottomMenu(true));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHeaderAndFooter();
        initView();
    }

    private void reloadAdapter() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {
                List<Song> songList = AudioUtils.getAllSongs();
                Collections.reverse(songList);
                mAdapter.setPlayList(songList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mAdapter.notifyDataSetChanged();
                mTvCount.setText(String.format(getString(R.string.title_song_count), mAdapter.getCount()));
            }
        }.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadAdapter();
    }

    private void initView() {
        mAdapter = new LocalListAdapter(getContext(), null);
        mAdapter.setFragmentManager(getFragmentManager());
        mRvSongs.setAdapter(mAdapter);
        mRvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!CommonUtil.isNotFastClick()){
                    return;
                }
                if (i == 0) {
                    return;
                }
                i -= 1;
                EventBus.getDefault().post(new ActionSelectSong((Song) mAdapter.getItem(i)));
            }
        });

        mTvTitle.setText(R.string.title_local_music);
        mIvHeader.setImageResource(R.mipmap.ic_local_header_bg);
        mIvIcon.setImageResource(R.mipmap.ic_local_tip);
   }

    private void initHeaderAndFooter() {
        View header = getLayoutInflater().inflate(R.layout.layout_recent_love_list_header, null);
        mRvSongs.addHeaderView(header);
        mTvCount = header.findViewById(R.id.tv_count);
        mTvTitle = header.findViewById(R.id.tv_title);
        mIvHeader = header.findViewById(R.id.iv_header);
        mIvIcon = header.findViewById(R.id.iv_icon);
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


}