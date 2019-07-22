package internet.com.larkmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import internet.com.larkmusic.R;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.player.MusicPlayer;
import internet.com.larkmusic.util.HistoryService;
import internet.com.larkmusic.util.RecentSongService;

/**
 * Created by sjning
 * created on: 2019-06-18 17:51
 * description:
 */
public class HistoryAdapter extends BaseAdapter {

    private List<String> nameList;
    private Context context;

    @Override
    public int getCount() {
        if (nameList != null) {
            return nameList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if (i >= 0 && nameList != null && nameList.size() > i) {
            return nameList.get(i);
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
            convertView = inflater.inflate(R.layout.layout_item_history, null, true);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        String name = nameList.get(i);
        holder.title.setText(name);
        holder.ivDelete.setOnClickListener(new MyClickListener(nameList.get(i), i));
        return convertView;
    }

    public class MyViewHolder {
        TextView title;
        ImageView ivDelete;

        public MyViewHolder(View view) {

            title = view.findViewById(R.id.tv_song);
            ivDelete = view.findViewById(R.id.iv_delete);
        }
    }

    public void setPlayList(List<String> playList) {
        this.nameList = playList;
    }


    public HistoryAdapter(Context ctx) {
        super();
        context = ctx;
    }

    public class MyClickListener implements View.OnClickListener {

        private String history;
        private int position;

        public MyClickListener(String history, int position) {
            this.history = history;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            HistoryService.getInstance().removeSong(history);
            notifyDataSetChanged();

        }
    }

}