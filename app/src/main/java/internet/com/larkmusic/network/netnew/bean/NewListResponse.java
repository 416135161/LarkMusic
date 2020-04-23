package internet.com.larkmusic.network.netnew.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjning
 * created on: 2019-07-26 14:14
 * description:
 */
public class NewListResponse implements Serializable {


    /**
     * msg : network error
     * result : [{"singermid":"004YWbes0huGOX","singername":"back number","songmid":"002zQIbX3PXrd7","albummid":"002V0sGo2lbOJp","albumid":"6524311",
     * "type":"4","songname":"最深部","playUrl":null,"albumname":"MAGIC","imgUrl":"https://y.gtimg.cn/music/photo_new/T002R90x90M000002V0sGo2lbOJp
     * .jpg?max_age=2592000","mvUrl":null,"size_flac":0,"media_mid":"002zQIbX3PXrd7","passtime":1564114262000,"id":1299,"page":0,"songid":"230625062"}]
     * success : true
     * rowCount : 100
     */

    private String msg;
    private boolean success;
    private int rowCount;
    private List<ResultBean> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable{
        /**
         * singermid : 004YWbes0huGOX
         * singername : back number
         * songmid : 002zQIbX3PXrd7
         * albummid : 002V0sGo2lbOJp
         * albumid : 6524311
         * type : 4
         * songname : 最深部
         * playUrl : null
         * albumname : MAGIC
         * imgUrl : https://y.gtimg.cn/music/photo_new/T002R90x90M000002V0sGo2lbOJp.jpg?max_age=2592000
         * mvUrl : null
         * size_flac : 0
         * media_mid : 002zQIbX3PXrd7
         * passtime : 1564114262000
         * id : 1299
         * page : 0
         * songid : 230625062
         */

        public String singermid;
        public String singername;
        public String songmid;
        public String albummid;
        public String albumid;
        public String type;
        public String songname;
        public Object playUrl;
        public String albumname;
        public String imgUrl;
        public Object mvUrl;
        public int size_flac;
        public String media_mid;
        public int id;
        public int page;
        public String songid;

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

        public String getSongmid() {
            return songmid;
        }

        public void setSongmid(String songmid) {
            this.songmid = songmid;
        }

        public String getAlbummid() {
            return albummid;
        }

        public void setAlbummid(String albummid) {
            this.albummid = albummid;
        }

        public String getAlbumid() {
            return albumid;
        }

        public void setAlbumid(String albumid) {
            this.albumid = albumid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSongname() {
            return songname;
        }

        public void setSongname(String songname) {
            this.songname = songname;
        }

        public Object getPlayUrl() {
            return playUrl;
        }

        public void setPlayUrl(Object playUrl) {
            this.playUrl = playUrl;
        }

        public String getAlbumname() {
            return albumname;
        }

        public void setAlbumname(String albumname) {
            this.albumname = albumname;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public Object getMvUrl() {
            return mvUrl;
        }

        public void setMvUrl(Object mvUrl) {
            this.mvUrl = mvUrl;
        }

        public int getSize_flac() {
            return size_flac;
        }

        public void setSize_flac(int size_flac) {
            this.size_flac = size_flac;
        }

        public String getMedia_mid() {
            return media_mid;
        }

        public void setMedia_mid(String media_mid) {
            this.media_mid = media_mid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public String getSongid() {
            return songid;
        }

        public void setSongid(String songid) {
            this.songid = songid;
        }
    }
}
