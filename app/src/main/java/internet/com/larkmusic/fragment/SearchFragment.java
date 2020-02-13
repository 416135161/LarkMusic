package internet.com.larkmusic.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionMainBottomMenu;
import internet.com.larkmusic.action.ActionSearchSinger;
import internet.com.larkmusic.action.ActionSearchSongs;
import internet.com.larkmusic.action.ActionSelectSong;
import internet.com.larkmusic.activity.MainActivity;
import internet.com.larkmusic.adapter.HistoryAdapter;
import internet.com.larkmusic.adapter.SearchListAdapter;
import internet.com.larkmusic.adapter.SingerListAdapter;
import internet.com.larkmusic.back.BackHandlerHelper;
import internet.com.larkmusic.back.FragmentBackHandler;
import internet.com.larkmusic.base.EventFragment;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.network.Config;
import internet.com.larkmusic.network.netnew.NewCloudDataUtil;
import internet.com.larkmusic.network.netnew.bean.SearchSingerResponse;
import internet.com.larkmusic.util.CommonUtil;
import internet.com.larkmusic.util.HistoryService;
import internet.com.larkmusic.util.ToastUtils;
import internet.com.larkmusic.view.FlowLayout;
import internet.com.larkmusic.view.MyListView;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.annotation.Dimension.SP;

/**
 * Created by sjning
 * created on: 2019/5/7 下午8:06
 * description:
 */
public class SearchFragment extends EventFragment implements FragmentBackHandler {
    final int searchTypeSinger = 1;
    final int searchTypeSong = 2;

    int mPageSongs = 0;
    final int PAGE_SIZE = 25;
    final int PAGE_MAX = 5;
    String mKeySongs = "";

    @BindView(R.id.rv_songs)
    MyListView mRvSongs;
    @BindView(R.id.rv_singer)
    ListView mRvSinger;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.view_condition)
    View mViewCondition;
    @BindView(R.id.flow)
    FlowLayout mFlowLayout;
    @BindView(R.id.tv_delete)
    TextView mTvDelete;
    @BindView(R.id.tv_singer_type)
    TextView mTvSingerType;
    @BindView(R.id.tv_song_type)
    TextView mTvSongType;
    SearchListAdapter mAdapter;
    HistoryAdapter mHistoryAdapter;
    SingerListAdapter mSingerAdapter;

    @BindView(R.id.rv_history)
    ListView mRvHistory;
    List<String> mValues;

    int searchType = searchTypeSong;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new SearchListAdapter(getContext(), new ArrayList<Song>());
        mRvSongs.setAdapter(mAdapter);
        mRvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!CommonUtil.isNotFastClick()){
                    return;
                }
                EventBus.getDefault().post(new ActionSelectSong((Song) mAdapter.getItem(i)));

            }
        });
        mRvSongs.setOnILoadListener(new MyListView.ILoadListener() {
            @Override
            public void loadData() {
                if(mPageSongs < PAGE_MAX){
                    NewCloudDataUtil.searchSongs(mKeySongs, mPageSongs, PAGE_SIZE);
                }
            }
        });

        mSingerAdapter = new SingerListAdapter(getContext(), new ArrayList<SearchSingerResponse.DataBean.SingerBean.Singer>());
        mRvSinger.setAdapter(mSingerAdapter);
        mRvSinger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!CommonUtil.isNotFastClick()){
                    return;
                }
                SearchSingerResponse.DataBean.SingerBean.Singer singer = (SearchSingerResponse.DataBean.SingerBean.Singer) mSingerAdapter.getItem(i);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                Fragment fragment = new SingerSongListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("singer", singer);
                fragment.setArguments(bundle);
                transaction.add(R.id.view_container, fragment);
                transaction.addToBackStack("");
                transaction.commit();
                EventBus.getDefault().post(new ActionMainBottomMenu(false));
          }
        });
        mHistoryAdapter = new HistoryAdapter(getContext());
        mRvHistory.setAdapter(mHistoryAdapter);
        mHistoryAdapter.setPlayList(HistoryService.getInstance().getList());
        mRvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                doSearch((String) mHistoryAdapter.getItem(i));
            }
        });

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    HistoryService.getInstance().saveSong(mEtSearch.getText().toString());
                    doSearch(mEtSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    mTvDelete.setVisibility(View.INVISIBLE);
                } else {
                    mTvDelete.setVisibility(View.VISIBLE);
                }
            }
        });
        initTrending();

        View footer2 = new View(getContext());
        footer2.setMinimumHeight(50);
        mRvHistory.addFooterView(footer2);
    }

    @OnClick(R.id.tv_cancel)
    void onClickCancel() {
        mEtSearch.setText("");
        mRvSongs.setVisibility(View.GONE);
        mRvSinger.setVisibility(View.GONE);
        mAdapter.setPlayList(null);
        mSingerAdapter.setPlayList(null);
        mViewCondition.setVisibility(View.VISIBLE);
        hideInput();
    }

    @OnClick(R.id.tv_delete)
    void onClickDelete() {
        mEtSearch.setText("");
    }

    @OnClick(R.id.tv_song_type)
    void onClickSongType() {
        searchType = searchTypeSong;
        mTvSongType.setTextColor(getResources().getColor(R.color.text_red));
        mTvSingerType.setTextColor(getResources().getColor(R.color.text_999));
        mRvSinger.setVisibility(View.GONE);
        mRvSongs.setVisibility(View.VISIBLE);
        mRvSongs.hideProgress();
        showInput(mEtSearch);
    }

    @OnClick(R.id.tv_singer_type)
    void onClickSingerType() {
        searchType = searchTypeSinger;
        mTvSongType.setTextColor(getResources().getColor(R.color.text_999));
        mTvSingerType.setTextColor(getResources().getColor(R.color.text_red));
        mRvSongs.setVisibility(View.GONE);
        mRvSinger.setVisibility(View.VISIBLE);
        showInput(mEtSearch);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPosting(ActionSearchSongs event) {
        closeDialog();
        if (event != null && event.result != null && event.result.size() > 0) {
            if(mPageSongs == 0){
                mAdapter.setPlayList(event.result);
                mRvSongs.smoothScrollToPosition(0);
                //越过第一页
                mPageSongs ++;
            }else {
                mAdapter.addPlayList(event.result);
            }
            mPageSongs ++;
        }else {
            if(mPageSongs == 0){
                ToastUtils.show(R.string.please_check_net);
                mRvSongs.hideProgress();
            }
        }
        if (mPageSongs < PAGE_MAX) {
            if(mAdapter.getCount() % PAGE_SIZE == 0){
                mRvSongs.loadFinish();
            }else {
                mRvSongs.loadFinish(false);
            }
        } else {
            mRvSongs.loadFinish(false);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPosting(ActionSearchSinger event) {
        if (event != null && event.singerList != null && event.singerList.size() > 0) {
            for(SearchSingerResponse.DataBean.SingerBean.Singer singer : event.singerList){
                singer.pic = "http://y.gtimg.cn/music/photo_new/T001R150x150M000%@.jpg".replace("%@", singer.mid);
            }
            mSingerAdapter.setPlayList(event.singerList);
        }
        closeDialog();
    }

    private void initTrending() {
        mValues = new ArrayList<>();
        //日本歌手
        if (Config.getFrom() == Config.FROM_JAPAN) {
            mValues.add("DAISHI DANCE");
            mValues.add("花澤香菜");
            mValues.add("澤野弘之");
            mValues.add("久石譲");
            mValues.add("倉木麻衣");
            mValues.add("Sexy Zone");
            mValues.add("majiko");
            mValues.add("RADWIMPS");
            mValues.add("新垣結衣");
            mValues.add("Aimer");
            mValues.add("DJ OKAWARI");
            mValues.add("高梨康治 ");
        } else if (Config.getFrom() == Config.FROM_CHINESE) {
            mValues.add("林俊杰");
            mValues.add("薛之谦");
            mValues.add("周杰伦");
            mValues.add("张杰");
            mValues.add("刘德华");
            mValues.add("邓紫棋");
            mValues.add("陈雪凝");
            mValues.add("庄心妍");
            mValues.add("孙露");
            mValues.add("张靓颖");
        } else {
            //欧美歌手
            mValues.add("Alan Walker");
            mValues.add("Taylor Swift");
            mValues.add("The Chainsmokers");
            mValues.add("Olly Murs");
            mValues.add("Vicetone");
            mValues.add("Taylor Swift");
            mValues.add("Zella Day");
            mValues.add("Shawn Mendes");
            mValues.add("Charlie Puth");
            mValues.add("Sia");
        }

        mEtSearch.setHint(mValues.get(0));

        //往容器内添加TextView数据
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 16, 20);
        if (mFlowLayout != null) {
            mFlowLayout.removeAllViews();
        }
        for (int i = 0; i < mValues.size(); i++) {
            TextView tv = new TextView(this.getActivity());
            tv.setPadding(20, 8, 20, 8);
            tv.setText(mValues.get(i));
            tv.setMaxEms(10);
            tv.setTextColor(0xff666666);
            tv.setTextSize(SP, 10);
            tv.setSingleLine();
            tv.setBackgroundResource(R.drawable.item_flow_bg);
            tv.setLayoutParams(layoutParams);
            mFlowLayout.addView(tv, layoutParams);
            final int position = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doSearch(mValues.get(position));
                }
            });
        }

    }

    private void doSearch(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        mEtSearch.setText(key);
        mEtSearch.setSelection(key.length());
        mViewCondition.setVisibility(View.GONE);
        hideInput();
        showDialog();
        if (searchType == searchTypeSong) {
            mKeySongs = key;
            mPageSongs = 0;
            NewCloudDataUtil.searchSongs(key, mPageSongs, PAGE_SIZE);
            mRvSongs.setVisibility(View.VISIBLE);
        } else {
            NewCloudDataUtil.searchSinger(key);
            mRvSinger.setVisibility(View.VISIBLE);
        }

        ((MainActivity)getActivity()).showAd(Config.TYPE_SEARCH_ADS);

    }

    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    public void showInput(final EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        View v = getActivity().getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            mEtSearch.setText("");
            mViewCondition.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onBackPressed() {
        if( BackHandlerHelper.handleBackPress(this)){
            return true;
        }
        if (mViewCondition.getVisibility() != View.VISIBLE) {
            mEtSearch.setText("");
            mViewCondition.setVisibility(View.VISIBLE);
            mAdapter.setPlayList(null);
            mSingerAdapter.setPlayList(null);
            return true;
        }
        return false;
    }
}
