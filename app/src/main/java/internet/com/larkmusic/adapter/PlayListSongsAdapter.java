package internet.com.larkmusic.adapter;

import android.content.ContentValues;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.List;

import internet.com.larkmusic.R;
import internet.com.larkmusic.action.ActionRefreshPlayList;
import internet.com.larkmusic.bean.PlayListBean;
import internet.com.larkmusic.bean.PlayListRelationBean;
import internet.com.larkmusic.bean.SavedStateBean;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.fragment.OperateDialog;
import internet.com.larkmusic.util.CommonUtil;

/**
 * Created by sjning
 * created on: 2019-07-21 21:20
 * description:
 */
public class PlayListSongsAdapter extends BaseAdapter {

    private List<Song> songs;
    private Context context;
    private String playListName;

    private FragmentManager mFragmentManager;

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
            convertView = inflater.inflate(R.layout.layout_item_play_list_songs, null, true);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        final Song song = songs.get(i);
        holder.title.setText(song.getSongName());
        holder.artist.setText(song.getSingerName());
        holder.no.setText((i + 1) + "");
        if (TextUtils.isEmpty(song.getImgUrl())) {
            song.setImgUrl(CommonUtil.getDBImage(song.getHash()));
        }
        Picasso.with(context)
                .load(song.getImgUrl())
                .error(R.mipmap.ic_song_default)
                .placeholder(R.mipmap.ic_song_default)
                .into(holder.art);
        holder.ivDelete.setOnClickListener(new MyClickListener(song, i));
        return convertView;
    }

    public class MyViewHolder {

        ImageView art, ivDelete;
        TextView no, title, artist;

        public MyViewHolder(View view) {
            art = view.findViewById(R.id.iv_icon);
            ivDelete = view.findViewById(R.id.iv_delete);
            no = view.findViewById(R.id.tv_no);
            title = view.findViewById(R.id.tv_song);
            artist = view.findViewById(R.id.tv_singer);
        }
    }

    public void setPlayList(List<Song> playList) {
        this.songs = playList;
    }


    public PlayListSongsAdapter(Context ctx, List<Song> Songs) {
        super();
        context = ctx;
        this.songs = Songs;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
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
                    setOnDeleteListener(new OperateDialog.OnDeleteListener() {
                        @Override
                        public void onDelete(Song song) {
                            songs.remove(position);
                            notifyDataSetChanged();
                            LitePal.deleteAll(PlayListRelationBean.class, "playListName = ? AND songHash = ?",
                                    playListName, song.getHash());
                            int count = LitePal.where("playListName = ?", playListName).count(PlayListRelationBean.class);
                            if (count == 0) {
                                ContentValues cv = new ContentValues();
                                cv.put("icon", "");
                                LitePal.updateAll(PlayListBean.class, cv, "name = ?", playListName);
                            } else {
                                if (getItem(0) != null) {
                                    Song song1 = (Song) getItem(0);
                                    ContentValues cv = new ContentValues();
                                    cv.put("icon", song1.getImgUrl());
                                    LitePal.updateAll(PlayListBean.class, cv, "name = ?", playListName);
                                }
                            }
                            EventBus.getDefault().post(new ActionRefreshPlayList());
                        }
                    }).
                    show(mFragmentManager, OperateDialog.class.getName());
        }
    }
}