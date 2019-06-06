package internet.com.larkmusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionPlayList;
import internet.com.larkmusic.action.ActionStartPlayAct;
import internet.com.larkmusic.adapter.SongListAdapter;
import internet.com.larkmusic.base.EventFragment;
import internet.com.larkmusic.bean.Album;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.network.Config;
import internet.com.larkmusic.util.BlurTransformation;
import internet.com.larkmusic.util.CloudDataUtil;

/**
 * Created by sjning
 * created on: 2019/5/17 下午6:36
 * description:
 */
public class SongListFragment extends EventFragment {
    private String from;
    private Album album;

    @BindView(R.id.rv_songs)
    ListView mRvSongs;
    private SongListAdapter mAdapter;

    TextView mTvTitle;
    TextView mTvCount;
    ImageView mIvHeader, mIvIcon;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_song_list;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        from = getArguments().getString("from", Config.FROM_US);
        album = (Album) getArguments().getSerializable("album");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHeaderAndFooter();
        initView();
        mTvTitle.setText(album.getName());
        Picasso.with(getContext())
                .load(album.getImgUrl())
                .error(R.mipmap.ic_song_default)
                .placeholder(R.mipmap.ic_song_default)
                .transform(new BlurTransformation(getActivity()))
                .into(mIvHeader);
        Picasso.with(getContext())
                .load(album.getImgUrl())
                .error(R.mipmap.ic_song_default)
                .placeholder(R.mipmap.ic_song_default)
                .into(mIvIcon);
        showDialog();
        CloudDataUtil.getPlayList(album.getId() + "", from);
    }

    private void initView() {
        mAdapter = new SongListAdapter(getContext(), new ArrayList<Song>());
        mRvSongs.setAdapter(mAdapter);
        mRvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                }
                i -= 1;
                EventBus.getDefault().post(new ActionStartPlayAct((Song) mAdapter.getItem(i)));
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
        View footer = new View(getContext());
        footer.setMinimumHeight(50);
        mRvSongs.addFooterView(footer);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceive(ActionPlayList event) {
        closeDialog();
        ArrayList<Song> playList = event.result;
        if (playList != null && !playList.isEmpty()) {
            mAdapter.setPlayList(playList);
            mAdapter.notifyDataSetChanged();
            mTvCount.setText(String.format(getString(R.string.title_song_count), mAdapter.getCount()));
        }
    }

}
