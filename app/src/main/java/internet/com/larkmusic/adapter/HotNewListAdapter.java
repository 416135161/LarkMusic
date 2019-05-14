package internet.com.larkmusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import internet.com.larkmusic.R;
import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2019/5/12 下午10:37
 * description:
 */
public class HotNewListAdapter extends RecyclerView.Adapter<HotNewListAdapter.MyViewHolder> {
    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    private int selectedPosition;
    private List<Song> songs;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView art, operate;
        TextView no, title, artist;

        public MyViewHolder(View view) {
            super(view);
            art = view.findViewById(R.id.iv_icon);
            operate = view.findViewById(R.id.iv_operate);
            no = view.findViewById(R.id.tv_no);
            title = view.findViewById(R.id.tv_song);
            artist = view.findViewById(R.id.tv_singer);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_hot_new, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Song song = songs.get(position);
        holder.title.setText(song.getSongName());
        holder.artist.setText(song.getSingerName());
        holder.no.setText((position + 1) + "");
        try {
            Picasso.with(context)
                    .load(song.getImgUrl())
                    .error(R.mipmap.ic_default)
                    .placeholder(R.mipmap.ic_default)
                    .into(holder.art);
        } catch (Exception e) {
           e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder.itemView, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (songs != null) {
            return songs.size();
        } else {
            return 0;
        }

    }

    public void setPlaylists(List<Song> playList) {
        this.songs = playList;
    }


    public HotNewListAdapter(Context ctx, List<Song> Songs) {
        super();
        context = ctx;
        this.songs = Songs;
    }

    public Song getData(int position) {
        if (position >= 0 && songs != null && songs.size() > position) {
            return songs.get(position);
        } else return null;
    }

    private OnItemClickListener onItemClickListener = null;

    /*暴露给外部的方法*/
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}