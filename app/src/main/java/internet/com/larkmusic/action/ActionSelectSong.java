package internet.com.larkmusic.action;

import java.util.List;

import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2019/5/29 下午5:25
 * description:
 */
public class ActionSelectSong {

    public Song song;
    public List<Song> songList;

    public ActionSelectSong(Song song) {
        this.song = song;
    }

    public ActionSelectSong(List<Song> songList) {
        this.songList = songList;
    }
}
