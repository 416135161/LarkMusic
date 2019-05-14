package internet.com.larkmusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import internet.com.larkmusic.R;
import internet.com.larkmusic.base.BaseFragment;
import internet.com.larkmusic.network.Config;
import internet.com.larkmusic.util.CommonUtil;

/**
 * Created by sjning
 * created on: 2019/5/7 下午8:06
 * description:
 */
public class SearchFragment extends BaseFragment {
    @BindView(R.id.id_flow_lay_out)
    TagFlowLayout mFlowLayout;
    @BindView(R.id.et_search)
    EditText mEtSearch;

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    List<String> mValues;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CommonUtil.setTvBoldFace(mTvTitle);
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {

                    Toast.makeText(getContext(), "呵呵", Toast.LENGTH_SHORT).show();
                    // search pressed and perform your functionality.
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

    private void doSearch() {

    }

}
