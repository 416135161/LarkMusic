package internet.com.larkmusic.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by sjning
 * created on: 2019/5/12 下午8:57
 * description:
 */
public class Song implements Serializable{
    private String songName;

    public int duration;

    private String playUrl;

    private String imgUrl;

    private String hash;

    private String singerName;

    private String lrc;

    private String portrait;

    private boolean isLocal;


    public String getSongName() {
        if(!TextUtils.isEmpty(songName) && songName.matches(".*\\(.*")){
            songName = songName.substring(0, songName.indexOf("("));
        }else if(!TextUtils.isEmpty(songName) && songName.matches(".*\\【.*")){
            songName = songName.substring(0, songName.indexOf("【"));
        }
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }
}
