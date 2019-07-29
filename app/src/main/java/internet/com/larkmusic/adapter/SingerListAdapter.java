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
import internet.com.larkmusic.network.netnew.bean.SearchSingerResponse;

/**
 * Created by sjning
 * created on: 2019-07-29 11:53
 * description:
 */
public class SingerListAdapter extends BaseAdapter {

    private List<SearchSingerResponse.DataBean.SingerBean.Singer> singerList;
    private Context context;

    @Override
    public int getCount() {
        if (singerList != null) {
            return singerList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if (i >= 0 && singerList != null && singerList.size() > i) {
            return singerList.get(i);
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
            convertView = inflater.inflate(R.layout.layout_item_singer, null, true);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        final SearchSingerResponse.DataBean.SingerBean.Singer singer = singerList.get(i);
        holder.title.setText(singer.name);
        Picasso.with(context)
                .load(singer.pic)
                .error(R.mipmap.ic_song_default)
                .placeholder(R.mipmap.ic_song_default)
                .into(holder.art);
       return convertView;
    }

    public class MyViewHolder {

        ImageView art, operate;
        TextView no, title, artist;

        public MyViewHolder(View view) {
            art = view.findViewById(R.id.iv_icon);
            operate = view.findViewById(R.id.iv_operate);
            no = view.findViewById(R.id.tv_no);
            title = view.findViewById(R.id.tv_song);
            artist = view.findViewById(R.id.tv_singer);
        }
    }

    public void setPlayList(List<SearchSingerResponse.DataBean.SingerBean.Singer> playList) {
        this.singerList = playList;
    }


    public SingerListAdapter(Context ctx, List<SearchSingerResponse.DataBean.SingerBean.Singer> Songs) {
        super();
        context = ctx;
        this.singerList = Songs;
    }

}