package internet.com.larkmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import internet.com.larkmusic.R;
import internet.com.larkmusic.bean.PlayListBean;

/**
 * Created by sjning
 * created on: 2019-07-20 09:07
 * description:
 */
public class PlayListAdapter extends BaseAdapter {

    private List<PlayListBean> playListBeans;
    private Context context;

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
        MyViewHolder holder;
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
        holder.no.setText(playListBean.getSongAmount() + "");
        Picasso.with(context)
                .load(playListBean.getIcon())
                .error(R.mipmap.ic_folder)
                .placeholder(R.mipmap.ic_folder)
                .into(holder.icon);
        holder.delete.setOnClickListener(new MyClickListener(playListBean, i));
        return convertView;
    }

    public class MyViewHolder {

        ImageView delete, icon;
        TextView no, name;

        public MyViewHolder(View view) {
            delete = view.findViewById(R.id.iv_delete);
            no = view.findViewById(R.id.tv_no);
            name = view.findViewById(R.id.tv_name);
            icon = view.findViewById(R.id.iv_icon);
        }
    }

    public void setPlayList(List<PlayListBean> playList) {
        this.playListBeans = playList;
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


        }
    }

}