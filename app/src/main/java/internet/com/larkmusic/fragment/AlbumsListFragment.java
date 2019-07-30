package internet.com.larkmusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionAlbumList;
import internet.com.larkmusic.adapter.AlbumListAdapter;
import internet.com.larkmusic.back.BackHandlerHelper;
import internet.com.larkmusic.back.FragmentBackHandler;
import internet.com.larkmusic.base.EventFragment;
import internet.com.larkmusic.bean.Album;
import internet.com.larkmusic.network.Config;
import internet.com.larkmusic.network.netnew.NewCloudDataUtil;

/**
 * Created by sjning
 * created on: 2019/5/17 上午11:55
 * description:
 */
public class AlbumsListFragment extends EventFragment implements FragmentBackHandler {
    private String from;
    private String name;
    private int srcId;
    @BindView(R.id.rv_albums)
    ListView mRvAlbums;
    private AlbumListAdapter mAdapter;

    TextView mTvTitle;
    TextView mTvCount;
    ImageView mIvHeader;

    public AlbumsListFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_albums_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        from = getArguments().getString("from", getString(R.string.from_us));
        name = getArguments().getString("name", "");
        srcId = getArguments().getInt("srcId", R.mipmap.ic_song_default);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHeaderAndFooter();
        initView();
        getData();
    }

    private void getData() {
        showDialog();
        NewCloudDataUtil.getPlayList(from);
    }

    private void initView() {
        mAdapter = new AlbumListAdapter(getContext(), new ArrayList<Album>());
        mRvAlbums.setAdapter(mAdapter);
        mRvAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                }
                i -= 1;
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                Fragment fragment = new SongListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", from);
                bundle.putSerializable("album", (Album) mAdapter.getItem(i));
                bundle.putSerializable("songs", ((Album) mAdapter.getItem(i)).songList);
                fragment.setArguments(bundle);
                transaction.add(R.id.view_container, fragment);
                transaction.addToBackStack("");
                transaction.commit();
            }
        });
    }

    private void initHeaderAndFooter() {
        View header = getLayoutInflater().inflate(R.layout.layout_album_list_header, null);
        mRvAlbums.addHeaderView(header);
        mTvCount = header.findViewById(R.id.tv_count);
        mTvTitle = header.findViewById(R.id.tv_title);
        header.findViewById(R.id.iv_play_all).setVisibility(View.GONE);
        mIvHeader = header.findViewById(R.id.iv_header);
        mIvHeader.setImageResource(srcId);
        mTvTitle.setText(name);
        mTvCount.setText(R.string.title_albums);
        View footer = new View(getContext());
        footer.setMinimumHeight(50);
        mRvAlbums.addFooterView(footer);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPosting(ActionAlbumList event) {
        closeDialog();
        ArrayList<Album> albumList = event.albumList;
        if (albumList != null && !albumList.isEmpty()) {
            if (TextUtils.equals(this.from, event.from)) {
                mAdapter.setPlayList(albumList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
    }


}
