package internet.com.larkmusic.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionMainBottomMenu;
import internet.com.larkmusic.action.ActionNewSongs;
import internet.com.larkmusic.action.ActionRefreshPlayList;
import internet.com.larkmusic.adapter.PlayListAdapter;
import internet.com.larkmusic.back.BackHandlerHelper;
import internet.com.larkmusic.back.FragmentBackHandler;
import internet.com.larkmusic.base.BaseFragment;
import internet.com.larkmusic.base.EventFragment;
import internet.com.larkmusic.bean.PlayListBean;
import internet.com.larkmusic.bean.SavedStateBean;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.util.CommonUtil;
import internet.com.larkmusic.util.ToastUtils;

/**
 * Created by sjning
 * created on: 2019-07-19 12:01
 * description:
 */
public class PlayListFragment extends EventFragment implements FragmentBackHandler {
    @BindView(R.id.rv_list)
    ListView mListView;
    PlayListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new ActionMainBottomMenu(true));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_play_list;
    }

    private void initView() {

        mAdapter = new PlayListAdapter(getContext(), null);
        mAdapter.setFragmentManager(getFragmentManager());
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!CommonUtil.isNotFastClick()){
                    return;
                }
                if (mAdapter.getItem(i) != null) {
                    PlayListBean playListBean = (PlayListBean) mAdapter.getItem(i);
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    Fragment fragment = new PlayListSongsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", playListBean);
                    fragment.setArguments(bundle);
                    transaction.add(R.id.view_container, fragment);
                    transaction.addToBackStack("");
                    transaction.commit();
                }
            }
        });
        refreshView();


    }

    @OnClick(R.id.tv_create_playlist)
    void onClickCreate(View view) {
        View contentView = getLayoutInflater().inflate(R.layout.dlg_add_palylist, null);
        final EditText editText = contentView.findViewById(R.id.et_name);
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.add_play_list))
                .setView(contentView)
                .setPositiveButton(getString(R.string.text_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(editText.getText().toString())) {
                            PlayListBean playListBean = new PlayListBean();
                            playListBean.setName(editText.getText().toString());
                            playListBean.saveOrUpdate("name = ?", playListBean.getName());
                            refreshView();
                        } else {
                            Toast.makeText(getContext(), R.string.add_play_list_empty_tip, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void refreshView() {
        LitePal.findAllAsync(PlayListBean.class).listen(new FindMultiCallback<PlayListBean>() {
            @Override
            public void onFinish(List<PlayListBean> list) {
                Collections.reverse(list);
                mAdapter.setPlayList(list);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPosting(ActionRefreshPlayList event) {
        closeDialog();
        refreshView();
    }
}
