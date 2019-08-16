package internet.com.larkmusic.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import internet.com.larkmusic.R;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.fragment.OperateDialog;
import internet.com.larkmusic.util.FileUtils;

/**
 * Created by sjning
 * created on: 2019-07-12 22:25
 * description:
 */
public class LocalListAdapter extends BaseAdapter {
    private FragmentManager mFragmentManager;
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
            convertView = inflater.inflate(R.layout.layout_item_local, null, true);
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
        holder.operate.setOnClickListener(new MyClickListener(song, i));
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
    }


    public LocalListAdapter(Context ctx, List<Song> Songs) {
        super();
        context = ctx;
        this.songs = Songs;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }


    public class MyClickListener implements View.OnClickListener {

        private Song song;
        private int position;

        public MyClickListener(Song song, int position) {
            this.song = song;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            new OperateDialog().setSong(song).
                    setShowDelete(true).
                    setShowDownload(false).
                    setOnDeleteListener(new OperateDialog.OnDeleteListener() {
                        @Override
                        public void onDelete(Song song) {
                            songs.remove(position);
                            notifyDataSetChanged();
                            FileUtils.delete(song.getPlayUrl());
                        }
                    }).
                    show(mFragmentManager, OperateDialog.class.getName());
        }
    }
}