package internet.com.larkmusic.action;

import java.util.ArrayList;

import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2019/1/14 上午9:55
 * description:
 */
public class ActionPlayList extends ActionBase{
    public ArrayList<Song> result;

    public ActionPlayList(ArrayList<Song> result) {
        this.result = result;
    }
}
