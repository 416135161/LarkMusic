package internet.com.larkmusic.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionMainBottomMenu;
import internet.com.larkmusic.action.ActionSelectSong;
import internet.com.larkmusic.action.ActionSingerSongs;
import internet.com.larkmusic.adapter.SongListAdapter;
import internet.com.larkmusic.base.EventFragment;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.network.netnew.NewCloudDataUtil;
import internet.com.larkmusic.network.netnew.bean.SearchSingerResponse;
import internet.com.larkmusic.util.BlurTransformation;
import internet.com.larkmusic.util.CommonUtil;
import internet.com.larkmusic.util.ToastUtils;
import internet.com.larkmusic.view.MyListView;

/**
 * Created by sjning
 * created on: 2019-08-04 21:37
 * description:
 */
public class SingerSongListFragment extends EventFragment {

    SearchSingerResponse.DataBean.SingerBean.Singer singer;

    int mPage = 0;
    final int PAGE_SIZE = 25;
    final int PAGE_MAX = 5;
    @BindView(R.id.rv_songs)
    MyListView mRvSongs;
    private SongListAdapter mAdapter;

    TextView mTvTitle;
    TextView mTvCount;
    ImageView mIvHeader, mIvIcon;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_singer_song_list;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singer = (SearchSingerResponse.DataBean.SingerBean.Singer) getArguments().getSerializable("singer");
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
        mTvTitle.setText(singer.name);
        Picasso.with(getContext())
                .load(singer.pic)
                .error(R.mipmap.ic_song_default)
                .placeholder(R.mipmap.ic_song_default)
                .transform(new BlurTransformation(getActivity()))
                .into(mIvHeader);
        Picasso.with(getContext())
                .load(singer.pic)
                .error(R.mipmap.ic_song_default)
                .placeholder(R.mipmap.ic_song_default)
                .into(mIvIcon);
        mTvCount.setText(String.format(getString(R.string.title_song_count), mAdapter.getCount()));
        showDialog();
        NewCloudDataUtil.getSingerSongs(singer.id, mPage, PAGE_SIZE);

    }

    private void initView() {
        mAdapter = new SongListAdapter(getContext(), new ArrayList<Song>());
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
        mRvSongs.setOnILoadListener(new MyListView.ILoadListener() {
            @Override
            public void loadData() {
                if (mPage < PAGE_MAX) {
                    NewCloudDataUtil.getSingerSongs(singer.id, mPage, PAGE_SIZE);
                }
            }
        });
    }

    private void initHeaderAndFooter() {
        View header = getLayoutInflater().inflate(R.layout.layout_song_list_header, null);
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceive(ActionSingerSongs event) {
        closeDialog();
        ArrayList<Song> playList = event.result;
        if (playList != null && !playList.isEmpty()) {
            if (mPage == 0) {
                mAdapter.setPlayList(playList);
                hideRefresh();
            } else {
                mAdapter.addPlayList(playList);
            }
            mAdapter.notifyDataSetChanged();
            mTvCount.setText(String.format(getString(R.string.title_song_count), mAdapter.getCount()));
            mPage++;
        } else {
            if (mPage == 0) {
                showRefresh();
                mRvSongs.hideProgress();
                ToastUtils.show(R.string.please_check_net);
            }
        }
        if (mPage < PAGE_MAX) {
            if(mAdapter.getCount() % PAGE_SIZE == 0){
                mRvSongs.loadFinish();
            }else {
                mRvSongs.loadFinish(false);
            }
        } else {
            mRvSongs.loadFinish(false);
        }
    }

    @Override
    protected void onRefresh() {
        showDialog();
        NewCloudDataUtil.getSingerSongs(singer.id, mPage, PAGE_SIZE);
    }

}
