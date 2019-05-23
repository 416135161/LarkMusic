package internet.com.larkmusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionSearchSongs;
import internet.com.larkmusic.adapter.SearchListAdapter;
import internet.com.larkmusic.base.EventFragment;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.util.CloudDataUtil;
import internet.com.larkmusic.util.CommonUtil;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by sjning
 * created on: 2019/5/7 下午8:06
 * description:
 */
public class SearchFragment extends EventFragment {
    @BindView(R.id.id_flow_lay_out)
    TagFlowLayout mFlowLayout;
    @BindView(R.id.et_search)
    EditText mEtSearch;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.rv_songs)
    ListView mRvSongs;
    SearchListAdapter mAdapter;
    List<String> mValues;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CommonUtil.setTvBoldFace(mTvTitle);

        View footer = new View(getContext());
        footer.setMinimumHeight(50);
        mRvSongs.addFooterView(footer);

        mAdapter = new SearchListAdapter(getContext(), new ArrayList<Song>());
        mRvSongs.setAdapter(mAdapter);
        mRvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {

                    Toast.makeText(getContext(), "呵呵", Toast.LENGTH_SHORT).show();
                    showDialog();
                    CloudDataUtil.searchSongs(mEtSearch.getText().toString());
                    hideInput();
                    return true;
                }

                return false;
            }
        });

        mValues = new ArrayList<>();
        mValues.add("eddad");
        mValues.add("eddad");
        mValues.add("eddad");
        mValues.add("eddad");
        mFlowLayout.setAdapter(new TagAdapter<String>(mValues) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.layout_item_flow,
                        mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
    }

    @OnClick(R.id.tv_cancel)
    void onClickCancel(View view) {
        mEtSearch.setText("");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPosting(ActionSearchSongs event) {
        closeDialog();
        if (event != null && event.result != null && event.result.size() > 0) {
            mAdapter.setPlayList(event.result);
            mAdapter.notifyDataSetChanged();

        }

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
