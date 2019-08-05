package internet.com.larkmusic.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionSelectSong;
import internet.com.larkmusic.adapter.RecentListHorizontalAdapter;
import internet.com.larkmusic.back.BackHandlerHelper;
import internet.com.larkmusic.back.FragmentBackHandler;
import internet.com.larkmusic.base.BaseFragment;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.listener.ClickItemTouchListener;
import internet.com.larkmusic.util.AudioUtils;
import internet.com.larkmusic.util.FavoriteService;
import internet.com.larkmusic.util.RecentSongService;

/**
 * Created by sjning
 * created on: 2019/5/7 下午8:19
 * description:
 */
public class MeFragment extends BaseFragment implements FragmentBackHandler {

    @BindView(R.id.rv_recent)
    RecyclerView mRvRecent;
    @BindView(R.id.tv_more)
    TextView mTvMore;

    @BindView(R.id.tv_library)
    TextView mTvLibrary;
    @BindView(R.id.tv_song)
    TextView mTvSong;
    @BindView(R.id.tv_song_count)
    TextView mTvSongCount;
    @BindView(R.id.tv_local_count)
    TextView mTvLocalCount;

    RecentListHorizontalAdapter mRecentAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRecentOrFavorite(RecentLoveFragment.TYPE_RECENT);
            }
        });

        mRecentAdapter = new RecentListHorizontalAdapter(RecentSongService.getInstance().getSongList(), getContext());
        mRvRecent.setNestedScrollingEnabled(false);
        LinearLayoutManager mLayoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRvRecent.setLayoutManager(mLayoutManager3);
        mRvRecent.setItemAnimator(new DefaultItemAnimator());

        mRvRecent.setAdapter(mRecentAdapter);

        mRvRecent.addOnItemTouchListener(new ClickItemTouchListener(mRvRecent) {
            @Override
            public boolean onClick(RecyclerView parent, View view, int position, long id) {
                EventBus.getDefault().post(new ActionSelectSong(mRecentAdapter.getItem(position)));
                return false;
            }

            @Override
            public boolean onLongClick(RecyclerView parent, View view, int position, long id) {
                return false;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRecentAdapter != null) {
            mRecentAdapter.notifyDataSetChanged();
        }
        int favoriteCount = FavoriteService.getInstance().getSongList().size();
        mTvSongCount.setText(String.format(getString(R.string.play_list_song_count), favoriteCount));

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(final Void... unused) {
                List<Song> songList = AudioUtils.getAllSongs();
                return songList.size();
            }

            @Override
            protected void onPostExecute(Integer aVoid) {
                mTvLocalCount.setText(String.format(getContext().getString(R.string.play_list_song_count), aVoid));
            }
        }.execute();

    }

    @OnClick(R.id.view_song)
    void onClickFavoriteSong(View view) {
        goToRecentOrFavorite(RecentLoveFragment.TYPE_FAVORITE_SONG);
    }

    private void goToRecentOrFavorite(int type) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        Fragment fragment = new RecentLoveFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("from", type);
        fragment.setArguments(bundle);
        transaction.add(R.id.view_container, fragment);
        transaction.addToBackStack("");
        transaction.commit();
    }


    @OnClick(R.id.view_library)
    void onClickFavoriteLibrary(View view) {

    }

    @OnClick(R.id.view_local)
    void onClickLocalSong(View view) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        Fragment fragment = new LocalSongsFragment();
        transaction.add(R.id.view_container, fragment);
        transaction.addToBackStack("");
        transaction.commit();

    }

    @OnClick(R.id.view_playlist)
    void onClickPlayList(View view) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        Fragment fragment = new PlayListFragment();
        transaction.add(R.id.view_container, fragment);
        transaction.addToBackStack("");
        transaction.commit();
    }

    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
    }

}
