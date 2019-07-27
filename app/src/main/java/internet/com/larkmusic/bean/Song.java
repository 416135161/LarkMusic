package internet.com.larkmusic.bean;

import android.text.TextUtils;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

import internet.com.larkmusic.network.netnew.bean.NewListResponse;
import internet.com.larkmusic.network.netnew.bean.PlayUrlRequest;

/**
 * Created by sjning
 * created on: 2019/5/12 下午8:57
 * description:
 */
public class Song extends LitePalSupport implements Serializable {
    @Column(unique = true, defaultValue = "unknown")
    private String hash;
    private String songName;
    public int duration;
    private String playUrl;
    private String imgUrl;
    private String singerName;
    private String lrc;
    private String portrait;
    @Column(ignore = true)
    private boolean isLocal;
    @Column(ignore = true)
    public PlayUrlRequest playUrlRequest;

    public String getSongName() {
        if (!TextUtils.isEmpty(songName) && songName.matches(".*\\(.*")) {
            songName = songName.substring(0, songName.indexOf("("));
        } else if (!TextUtils.isEmpty(songName) && songName.matches(".*\\【.*")) {
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

    @Override
    public boolean saveOrUpdate(String... conditions) {
        if(playUrlRequest != null){
            playUrlRequest.saveOrUpdate("songmid = ?", playUrlRequest.songmid);
        }
        return super.saveOrUpdate(conditions);
    }
}
