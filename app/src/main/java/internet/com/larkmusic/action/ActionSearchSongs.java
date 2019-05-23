package internet.com.larkmusic.action;

import java.util.ArrayList;

import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2019/5/22 下午5:39
 * description:
 */
public class ActionSearchSongs {

    public ArrayList<Song> result;

    public ActionSearchSongs(ArrayList<Song> result) {
        this.result = result;
    }
}
