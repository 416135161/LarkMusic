package internet.com.larkmusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import internet.com.larkmusic.action.ActionSearchSongs;
import internet.com.larkmusic.action.ActionSelectSong;
import internet.com.larkmusic.adapter.HistoryAdapter;
import internet.com.larkmusic.adapter.SearchListAdapter;
import internet.com.larkmusic.base.EventFragment;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.util.CloudDataUtil;
import internet.com.larkmusic.util.HistoryService;
import internet.com.larkmusic.view.FlowLayout;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.support.annotation.Dimension.SP;

/**
 * Created by sjning
 * created on: 2019/5/7 下午8:06
 * description:
 */
public class SearchFragment extends EventFragment {
    @BindView(R.id.rv_songs)
    ListView mRvSongs;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.view_condition)
    View mViewCondition;
    @BindView(R.id.flow)
    FlowLayout mFlowLayout;
    SearchListAdapter mAdapter;
    HistoryAdapter mHistoryAdapter;

    @BindView(R.id.rv_history)
    ListView mRvHistory;
    List<String> mValues;

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
                EventBus.getDefault().post(new ActionSelectSong((Song) mAdapter.getItem(i)));

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
        initTrending();
        View footer = new View(getContext());
        footer.setMinimumHeight(50);
        mRvSongs.addFooterView(footer);
    }

    @OnClick(R.id.tv_cancel)
    void onClickCancel() {
        mEtSearch.setText("");
        mViewCondition.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPosting(ActionSearchSongs event) {
        closeDialog();
        if (event != null && event.result != null && event.result.size() > 0) {
            mAdapter.setPlayList(event.result);
            mAdapter.notifyDataSetChanged();

        }
    }

    private void initTrending() {
        mValues = new ArrayList<>();
        //欧美个数
        mValues.add("Alan Walker");
        mValues.add("Taylor Swift");
        mValues.add("The Chainsmokers");
        mValues.add("Olly Murs");
        mValues.add("Vicetone");
//日本歌手
        mValues.add("ボーカロイド");
        mValues.add("泽野弘之");
        mValues.add("手嶌葵");
        mValues.add("山下智久");
        mValues.add("おぐり しゅん");
        mValues.add("石原さとみ");
        mValues.add("新垣結衣");

        //往容器内添加TextView数据
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 16, 16);
        if (mFlowLayout != null) {
            mFlowLayout.removeAllViews();
        }
        for (int i = 0; i < mValues.size(); i++) {
            TextView tv = new TextView(this.getActivity());
            tv.setPadding(20, 5, 20, 5);
            tv.setText(mValues.get(i));
            tv.setMaxEms(10);
            tv.setTextColor(0xff666666);
            tv.setTextSize(SP, 14);
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
        mEtSearch.setText(key);
        mViewCondition.setVisibility(View.GONE);
        hideInput();
        showDialog();
        CloudDataUtil.searchSongs(key);

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

}
