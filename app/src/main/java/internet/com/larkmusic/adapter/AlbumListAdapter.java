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
import internet.com.larkmusic.bean.Album;

/**
 * Created by sjning
 * created on: 2019/5/17 下午4:36
 * description:
 */
public class AlbumListAdapter extends BaseAdapter {

    private List<Album> albums;
    private Context context;

    @Override
    public int getCount() {
        if (albums != null) {
            return albums.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if (i >= 0 && albums != null && albums.size() > i) {
            return albums.get(i);
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
            convertView = inflater.inflate(R.layout.layout_item_album_list, null, true);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        final Album song = albums.get(i);
        holder.title.setText(song.getName());
        holder.artist.setVisibility(View.GONE);
        holder.no.setText((i + 1) + "");
        try {
            Picasso.with(context)
                    .load(song.getImgUrl())
                    .error(R.mipmap.ic_song_default)
                    .placeholder(R.mipmap.ic_song_default)
                    .into(holder.art);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void setPlayList(List<Album> playList) {
        this.albums = playList;
    }


    public AlbumListAdapter(Context ctx, List<Album> Songs) {
        super();
        context = ctx;
        this.albums = Songs;
    }

}