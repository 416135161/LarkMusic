package internet.com.larkmusic.action;

import java.util.List;

import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2019/2/13 上午10:35
 * description:
 */
public class ActionNewSongs {
    public static final int TYPE_HOME = 0;
    public static final int TYPE_LIST = 1;
    public boolean isOK;
    public List<Song> trackList;
    public int type;
    public String from;
}
