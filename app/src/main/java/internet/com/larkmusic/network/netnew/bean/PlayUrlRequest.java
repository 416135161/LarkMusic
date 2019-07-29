package internet.com.larkmusic.network.netnew.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Created by sjning
 * created on: 2019-07-26 14:37
 * description:
 */
public class PlayUrlRequest extends LitePalSupport implements Serializable {
    public String appid = "11110";

    /**
     * songmid :
     * songMediaId :
     * songName :
     * singermid :
     * singername :
     * flac : 0
     * albummid :
     * albumname :
     */

    public String songmid;
    public String songMediaId;
    public String songName;
    public String singermid;
    public String singername;
    public String flac = "0";
    public String albummid;
    public String albumname;

    public String getSongmid() {
        return songmid;
    }

    public void setSongmid(String songmid) {
        this.songmid = songmid;
    }

    public String getSongMediaId() {
        return songMediaId;
    }

    public void setSongMediaId(String songMediaId) {
        this.songMediaId = songMediaId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSingermid() {
        return singermid;
    }

    public void setSingermid(String singermid) {
        this.singermid = singermid;
    }

    public String getSingername() {
        return singername;
    }

    public void setSingername(String singername) {
        this.singername = singername;
    }

    public String getFlac() {
        return flac;
    }

    public void setFlac(String flac) {
        this.flac = flac;
    }

    public String getAlbummid() {
        return albummid;
    }

    public void setAlbummid(String albummid) {
        this.albummid = albummid;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }
}
