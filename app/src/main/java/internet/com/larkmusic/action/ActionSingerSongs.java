package internet.com.larkmusic.action;

import java.util.ArrayList;

import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.network.netnew.bean.SearchSingerResponse;

/**
 * Created by sjning
 * created on: 2019-07-29 15:09
 * description:
 */
public class ActionSingerSongs {
    public ArrayList<Song> result;
    public SearchSingerResponse.DataBean.SingerBean.Singer singer;

    public ActionSingerSongs(ArrayList<Song> result) {
        this.result = result;
    }

    public ActionSingerSongs(ArrayList<Song> result, SearchSingerResponse.DataBean.SingerBean.Singer singer) {
        this.result = result;
        this.singer = singer;
    }
}
