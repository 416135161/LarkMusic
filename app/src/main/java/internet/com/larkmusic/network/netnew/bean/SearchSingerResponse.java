package internet.com.larkmusic.network.netnew.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjning
 * created on: 2019-07-29 11:09
 * description:
 */
public class SearchSingerResponse implements Serializable {


    /**
     * code : 0
     * data : {"singer":{"count":2,"itemlist":[{"docid":"6499","id":"6499","mid":"002azErJ0UcDN6","name":"张杰","pic":"http://imgcache.qq
     * .com/music/photo/mid_singer_58/N/6/002azErJ0UcDN6.jpg","singer":"张杰"},{"docid":"174","id":"174","mid":"004Be55m1SJaLk","name":"张学友",
     * "pic":"http://imgcache.qq.com/music/photo/mid_singer_120/L/k/004Be55m1SJaLk.jpg","singer":"张学友"}],"name":"歌手","order":1,"type":2}}
     * subcode : 0
     */

    public int code;
    public DataBean data;

    public static class DataBean {
        /**
         * singer : {"count":2,"itemlist":[{"docid":"6499","id":"6499","mid":"002azErJ0UcDN6","name":"张杰","pic":"http://imgcache.qq
         * .com/music/photo/mid_singer_58/N/6/002azErJ0UcDN6.jpg","singer":"张杰"},{"docid":"174","id":"174","mid":"004Be55m1SJaLk","name":"张学友",
         * "pic":"http://imgcache.qq.com/music/photo/mid_singer_120/L/k/004Be55m1SJaLk.jpg","singer":"张学友"}],"name":"歌手","order":1,"type":2}
         */

        public SingerBean singer;

        public static class SingerBean {
            /**
             * count : 2
             * itemlist : [{"docid":"6499","id":"6499","mid":"002azErJ0UcDN6","name":"张杰","pic":"http://imgcache.qq
             * .com/music/photo/mid_singer_58/N/6/002azErJ0UcDN6.jpg","singer":"张杰"},{"docid":"174","id":"174","mid":"004Be55m1SJaLk","name":"张学友",
             * "pic":"http://imgcache.qq.com/music/photo/mid_singer_120/L/k/004Be55m1SJaLk.jpg","singer":"张学友"}]
             * name : 歌手
             * order : 1
             * type : 2
             */

            public int count;
            public String name;
            public int order;
            public int type;
            public List<Singer> itemlist;

            public static class Singer {
                /**
                 * docid : 6499
                 * id : 6499
                 * mid : 002azErJ0UcDN6
                 * name : 张杰
                 * pic : http://imgcache.qq.com/music/photo/mid_singer_58/N/6/002azErJ0UcDN6.jpg
                 * singer : 张杰
                 */

                public String docid;
                public String id;
                public String mid;
                public String name;
                public String pic;
                public String singer;
            }
        }
    }
}
