package internet.com.larkmusic.fragment;

import android.database.DataSetObserver;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionSelectSong;
import internet.com.larkmusic.adapter.PlayListSongsAdapter;
import internet.com.larkmusic.base.BaseFragment;
import internet.com.larkmusic.bean.PlayListBean;
import internet.com.larkmusic.bean.PlayListRelationBean;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.network.netnew.bean.PlayUrlRequest;
import internet.com.larkmusic.util.CommonUtil;

/**
 * Created by sjning
 * created on: 2019-07-21 21:34
 * description:
 */
public class PlayListSongsFragment extends BaseFragment {
    @BindView(R.id.rv_songs)
    ListView mRvSongs;
    private PlayListSongsAdapter mAdapter;


    TextView mTvTitle;
    TextView mTvCount;
    ImageView mIvHeader;
    ImageView mIvIcon;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot_new_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHeaderAndFooter();
        initView();
    }

    private void initView() {
        mAdapter = new PlayListSongsAdapter(getContext(), null);
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
        PlayListBean playListBean = (PlayListBean) getArguments().getSerializable("data");
        mTvTitle.setText(playListBean.getName());
        mIvHeader.setImageResource(R.mipmap.ic_playlist_header_bg);
        mIvIcon.setImageResource(R.mipmap.ic_playlist_tip);
        List<PlayListRelationBean> playListRelationBeanList = LitePal.where("playListName = ?", playListBean.getName())
                .find(PlayListRelationBean.class);
        if (playListRelationBeanList != null && playListRelationBeanList.size() > 0) {
            List<Song> songList = new ArrayList<>();
            for (PlayListRelationBean playListRelationBean : playListRelationBeanList) {
                Song song = LitePal.where("hash = ?", playListRelationBean.getSongHash()).findFirst(Song.class);
                if (song != null) {
                    song.playUrlRequest = LitePal.where("songmid = ?", song.getHash()).findFirst(PlayUrlRequest.class);
                    songList.add(song);
                }
            }
            Collections.reverse(songList);
            mAdapter.setPlayListName(playListBean.getName());
            mAdapter.setPlayList(songList);
            mTvCount.setText(String.format(getString(R.string.title_song_count), mAdapter.getCount()));
            Picasso.with(getContext())
                    .load(songList.get(0).getImgUrl())
                    .error(R.mipmap.ic_playlist_tip)
                    .placeholder(R.mipmap.ic_playlist_tip)
                    .into(mIvIcon);
        }else {
            mTvCount.setText(String.format(getString(R.string.title_song_count), 0));
        }
        mAdapter.notifyDataSetChanged();
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mTvCount.setText(String.format(getString(R.string.title_song_count), mAdapter.getCount()));
            }
        });
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
