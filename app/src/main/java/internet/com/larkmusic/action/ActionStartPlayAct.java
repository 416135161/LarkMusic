package internet.com.larkmusic.action;

import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2019/5/29 下午5:25
 * description:
 */
public class ActionStartPlayAct {

    public Song song;

    public ActionStartPlayAct(Song song) {
        this.song = song;
    }
}
