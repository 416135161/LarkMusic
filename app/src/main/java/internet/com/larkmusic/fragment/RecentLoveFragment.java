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

import butterknife.BindView;
import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionSelectSong;
import internet.com.larkmusic.adapter.HotNewListAdapter;
import internet.com.larkmusic.base.BaseFragment;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.util.BlurTransformation;
import internet.com.larkmusic.util.FavoriteService;
import internet.com.larkmusic.util.RecentSongService;

/**
 * Created by sjning
 * created on: 2019-06-24 14:54
 * description:
 */
public class RecentLoveFragment extends BaseFragment {
    private int type;
    public static int TYPE_RECENT = 0;
    public static int TYPE_FAVORITE_SONG = 1;
    public static int TYPE_FAVORITE_ALBUM = 2;

    @BindView(R.id.rv_songs)
    ListView mRvSongs;
    private HotNewListAdapter mAdapter;


    TextView mTvTitle;
    TextView mTvCount;
    ImageView mIvHeader;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot_new_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("from", TYPE_RECENT);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHeaderAndFooter();
        initView();

    }

    private void initView() {
        mAdapter = new HotNewListAdapter(getContext(), null);
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
        if (type == TYPE_RECENT) {
            mTvTitle.setText(R.string.title_recent);
            mAdapter.setPlayList(RecentSongService.getInstance().getSongList());
            Picasso.with(getContext())
                    .load(R.mipmap.ic_recent_header_bg)
                    .error(R.mipmap.ic_song_default)
                    .placeholder(R.mipmap.ic_song_default)
                    .transform(new BlurTransformation(getActivity()))
                    .into(mIvHeader);
        } else if (type == TYPE_FAVORITE_SONG) {
            mTvTitle.setText(R.string.title_favorite_music);
            mAdapter.setPlayList(FavoriteService.getInstance().getSongList());
            Picasso.with(getContext())
                    .load(R.mipmap.ic_favorite_header_bg)
                    .error(R.mipmap.ic_song_default)
                    .placeholder(R.mipmap.ic_song_default)
                    .transform(new BlurTransformation(getActivity()))
                    .into(mIvHeader);
        } else if (type == TYPE_FAVORITE_ALBUM) {
            mTvTitle.setText(R.string.title_favorite_album);
        }
        mAdapter.notifyDataSetChanged();
        mTvCount.setText(String.format(getString(R.string.title_song_count), mAdapter.getCount()));
    }

    private void initHeaderAndFooter() {
        View header = getLayoutInflater().inflate(R.layout.layout_hot_new_list_header, null);
        mRvSongs.addHeaderView(header);
        mTvCount = header.findViewById(R.id.tv_count);
        mTvTitle = header.findViewById(R.id.tv_title);
        mIvHeader = header.findViewById(R.id.iv_header);

        View footer = new View(getContext());
        footer.setMinimumHeight(50);
        mRvSongs.addFooterView(footer);
    }


}