package internet.com.larkmusic.action;

import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2019/6/4 上午11:07
 * description:
 */
public class ActionPlayerInformEvent {

    public enum Action {
        PLAYING, PREPARE, STOP
    }
    public Action action;
    public int currentTime;
    public int duration;
    public Song song;

    public ActionPlayerInformEvent() {
    }
}
