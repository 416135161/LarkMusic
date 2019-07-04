package internet.com.larkmusic.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionShowOperateDlg;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.player.MusicPlayer;

/**
 * Created by sjning
 * created on: 2019-07-04 16:26
 * description:
 */
public class PlayingListAdapter extends BaseAdapter {

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
            convertView = inflater.inflate(R.layout.layout_item_playling_list, null, true);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        final Song song = songs.get(i);
        holder.title.setText(song.getSongName());
        holder.artist.setText(song.getSingerName());
        if (MusicPlayer.getPlayer().getNowPlaying() != null &&
                TextUtils.equals(MusicPlayer.getPlayer().getNowPlaying().getHash(), song.getHash())) {
            holder.no.setTextColor(context.getResources().getColor(R.color.text_red));
            holder.title.setTextColor(context.getResources().getColor(R.color.text_red));
            holder.artist.setTextColor(context.getResources().getColor(R.color.text_red));
            holder.delete.setVisibility(View.INVISIBLE);
        } else {
            holder.no.setTextColor(context.getResources().getColor(R.color.text_666));
            holder.title.setTextColor(context.getResources().getColor(R.color.text_666));
            holder.artist.setTextColor(context.getResources().getColor(R.color.text_999));
            holder.delete.setVisibility(View.VISIBLE);
        }

        holder.no.setText((i + 1) + "");
        holder.delete.setOnClickListener(new MyClickListener(song, i));
        return convertView;
    }

    public class MyViewHolder {

        ImageView delete;
        TextView no, title, artist;

        public MyViewHolder(View view) {
            delete = view.findViewById(R.id.iv_delete);
            no = view.findViewById(R.id.tv_no);
            title = view.findViewById(R.id.tv_song);
            artist = view.findViewById(R.id.tv_singer);
        }
    }

    public void setPlayList(List<Song> playList) {
        this.songs = playList;
    }


    public PlayingListAdapter(Context ctx, List<Song> Songs) {
        super();
        context = ctx;
        this.songs = Songs;
    }

    public List<Song> getSongs() {
        return songs;
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
            songs.remove(position);
            notifyDataSetChanged();
            MusicPlayer.getPlayer().deleteIfExist(song);
        }
    }

}