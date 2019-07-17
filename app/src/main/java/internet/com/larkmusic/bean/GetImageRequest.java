package internet.com.larkmusic.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjning
 * created on: 2019-07-17 17:55
 * description:
 */
public class GetImageRequest implements Serializable {

    /**
     * data : [{"hash":"6A95093D434251A067CFB2F435D59856","audio_id":0,"album_audio_id":0}]
     * appid : 1155
     * mid : 4d031d0d0c33a8d8d8cb1929571114d9
     * clientver : 292
     * clienttime : 1563332919372
     * key : 0ecaaa197c5554669470ce7a5f77db60
     */

    private int appid = 1155;
    private String mid = "4d031d0d0c33a8d8d8cb1929571114d9";
    private int clientver = 292;
    private long clienttime = System.currentTimeMillis();
    private String key = "0ecaaa197c5554669470ce7a5f77db60";
    private List<DataBean> data = new ArrayList<>();

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public int getClientver() {
        return clientver;
    }

    public void setClientver(int clientver) {
        this.clientver = clientver;
    }

    public long getClienttime() {
        return clienttime;
    }

    public void setClienttime(long clienttime) {
        this.clienttime = clienttime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * hash : 6A95093D434251A067CFB2F435D59856
         * audio_id : 0
         * album_audio_id : 0
         */

        private String hash = "6A95093D434251A067CFB2F435D59856";
        private int audio_id = 0;
        private int album_audio_id = 0;

        public DataBean(String hash) {
            this.hash = hash;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public int getAudio_id() {
            return audio_id;
        }

        public void setAudio_id(int audio_id) {
            this.audio_id = audio_id;
        }

        public int getAlbum_audio_id() {
            return album_audio_id;
        }

        public void setAlbum_audio_id(int album_audio_id) {
            this.album_audio_id = album_audio_id;
        }
    }
}
