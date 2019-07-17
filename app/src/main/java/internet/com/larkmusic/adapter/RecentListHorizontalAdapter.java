package internet.com.larkmusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import internet.com.larkmusic.R;
import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2019-06-26 19:36
 * description:
 */
public class RecentListHorizontalAdapter extends RecyclerView.Adapter<RecentListHorizontalAdapter.MyViewHolder> {

    private List<Song> songList;
    Context ctx;


    public RecentListHorizontalAdapter(List<Song> songList, Context ctx) {
        this.songList = songList;
        this.ctx = ctx;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Song t = songList.get(position);
        Picasso.with(ctx)
                .load(t.getImgUrl())
                .error(R.mipmap.ic_song_cover_default)
                .placeholder(R.mipmap.ic_song_cover_default)
                .into(holder.art);
        holder.title.setText(t.getSongName());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public Song getItem(int i) {
        if (i >= 0 && songList != null && songList.size() > i) {
            return songList.get(i);
        } else return null;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView art;
        TextView title;


        public MyViewHolder(View view) {
            super(view);
            art = view.findViewById(R.id.backImage);
            title = view.findViewById(R.id.card_title);

        }
    }

}