package internet.com.larkmusic.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Created by sjning
 * created on: 2019-07-19 11:40
 * description:
 */
public class PlayListRelationBean extends LitePalSupport implements Serializable {

    private String playListName;
    private String songHash;


    public String getPlayListName() {
        return playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }

    public String getSongHash() {
        return songHash;
    }

    public void setSongHash(String songHash) {
        this.songHash = songHash;
    }
}
