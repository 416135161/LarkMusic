package internet.com.larkmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import internet.com.larkmusic.R;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.listener.MyClickListener;
import internet.com.larkmusic.util.CloudDataUtil;

/**
 * Created by sjning
 * created on: 2019/5/12 下午8:56
 * description:
 */
public class SearchListAdapter extends BaseAdapter {

    private List<Song> songs;
    private Context context;

    @Override
    public int getCount() {
        if (songs != null) {
            return songs.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if (i >= 0 && songs != null && songs.size() > i) {
            return songs.get(i);
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
            convertView = inflater.inflate(R.layout.layout_item_search, null, true);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        final Song song = songs.get(i);
        holder.title.setText(song.getSongName());
        holder.artist.setText(song.getSingerName());
        holder.no.setText((i + 1) + "");
        Picasso.with(context)
                .load(song.getImgUrl())
                .error(R.mipmap.ic_song_default)
                .placeholder(R.mipmap.ic_song_default)
                .into(holder.art);
        holder.operate.setOnClickListener(new MyClickListener(song));
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

    public void setPlayList(List<Song> playList) {
        this.songs = playList;
        notifyDataSetChanged();
    }

    public void addPlayList(List<Song> playList) {
        if (playList != null && songs != null) {
            songs.addAll(playList);
        }
        notifyDataSetChanged();
    }


    public SearchListAdapter(Context ctx, List<Song> Songs) {
        super();
        context = ctx;
        this.songs = Songs;
    }

}