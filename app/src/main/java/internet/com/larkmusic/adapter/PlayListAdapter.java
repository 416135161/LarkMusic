package internet.com.larkmusic.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.litepal.LitePal;
import org.litepal.crud.callback.CountCallback;

import java.util.List;

import internet.com.larkmusic.R;
import internet.com.larkmusic.bean.PlayListBean;
import internet.com.larkmusic.bean.PlayListRelationBean;
import internet.com.larkmusic.fragment.PlayListOperateDialog;
import internet.com.larkmusic.fragment.PlayingListDialog;

/**
 * Created by sjning
 * created on: 2019-07-20 09:07
 * description:
 */
public class PlayListAdapter extends BaseAdapter {

    private List<PlayListBean> playListBeans;
    private Context context;
    private FragmentManager mFragmentManager;

    @Override
    public int getCount() {
        if (playListBeans != null) {
            return playListBeans.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if (i >= 0 && playListBeans != null && playListBeans.size() > i) {
            return playListBeans.get(i);
        } else return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final MyViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.layout_item_play_list, null, true);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        final PlayListBean playListBean = playListBeans.get(i);
        holder.name.setText(playListBean.getName());
        LitePal.where("playListName = ?", playListBean.getName()).countAsync(PlayListRelationBean.class).listen(new CountCallback() {
            @Override
            public void onFinish(int count) {
                playListBean.setSongAmount(count);
                holder.no.setText(String.format(context.getString(R.string.play_list_song_count), playListBean.getSongAmount()));

            }
        });
        if (!TextUtils.isEmpty(playListBean.getIcon())) {
            Picasso.with(context)
                    .load(playListBean.getIcon())
                    .error(R.mipmap.ic_folder)
                    .placeholder(R.mipmap.ic_folder)
                    .into(holder.icon);
        } else {
            holder.icon.setImageResource(R.mipmap.ic_folder);
        }
        if (mFragmentManager == null) {
            holder.ivOperate.setVisibility(View.GONE);
        }
        holder.ivOperate.setOnClickListener(new MyClickListener(playListBean, i));
        return convertView;
    }

    public class MyViewHolder {

        ImageView ivOperate, icon;
        TextView no, name;

        public MyViewHolder(View view) {
            ivOperate = view.findViewById(R.id.iv_operate);
            no = view.findViewById(R.id.tv_no);
            name = view.findViewById(R.id.tv_name);
            icon = view.findViewById(R.id.iv_icon);
        }
    }

    public void setPlayList(List<PlayListBean> playList) {
        this.playListBeans = playList;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    public PlayListAdapter(Context ctx, List<PlayListBean> playListBeans) {
        super();
        context = ctx;
        this.playListBeans = playListBeans;
    }

    public class MyClickListener implements View.OnClickListener {

        private PlayListBean playListBean;
        private int position;

        public MyClickListener(PlayListBean playListBean, int position) {
            this.playListBean = playListBean;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            new PlayListOperateDialog().setSong(playListBean).
                    setOnDeleteListener(new PlayListOperateDialog.OnDeleteListener() {
                        @Override
                        public void onDelete(PlayListBean song) {


                        }
                    }).
                    show(mFragmentManager, PlayingListDialog.class.getName());


        }
    }

}