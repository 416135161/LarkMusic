package internet.com.larkmusic.network.netnew.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjning
 * created on: 2019-07-30 11:56
 * description:
 */
public class PlayListResponse implements Serializable {


    /**
     * msg : network error
     * result : [{"imgUrl":"http://p.qpic.cn/music_cover/bof4MDsSxjG6Va3xXJrNAGk5nDwflHw4er18BV1KARY3PsdBQlfupw/600?n=1","dissid":"7086552926",
     * "createtime":1562860800000,"passtime":1564141131000,"name":null,"count":"20265","id":"7086552926","page":0,"items":[{"dissid":"7086552926",
     * "singermid":"000BUomX2AwnMH","singer":"织田裕二","songmid":"0001bnVH2RJHWx","albummid":"0002vI8P4FT0tC","albumid":"33059","songname":"君のいる朝",
     * "playUrl":null,"albumname":"ありがとう","imgUrl":"https://y.gtimg.cn/music/photo_new/T002R90x90M0000002vI8P4FT0tC.jpg?max_age=2592000","mvUrl":null,
     * "size_flac":0,"media_mid":"002aSmzz25PAc8","id":324349,"songid":"408872"}],"categoryId":"169"}]
     * success : true
     * rowCount : 1653
     */

    public String msg;
    public boolean success;
    public int rowCount;
    public List<ResultBean> result;

    public static class ResultBean implements Serializable{
        /**
         * imgUrl : http://p.qpic.cn/music_cover/bof4MDsSxjG6Va3xXJrNAGk5nDwflHw4er18BV1KARY3PsdBQlfupw/600?n=1
         * dissid : 7086552926
         * createtime : 1562860800000
         * passtime : 1564141131000
         * name : null
         * count : 20265
         * id : 7086552926
         * page : 0
         * items : [{"dissid":"7086552926","singermid":"000BUomX2AwnMH","singer":"织田裕二","songmid":"0001bnVH2RJHWx","albummid":"0002vI8P4FT0tC",
         * "albumid":"33059","songname":"君のいる朝","playUrl":null,"albumname":"ありがとう","imgUrl":"https://y.gtimg.cn/music/photo_new/T002R90x90M0000002vI8P4FT0tC
         * .jpg?max_age=2592000","mvUrl":null,"size_flac":0,"media_mid":"002aSmzz25PAc8","id":324349,"songid":"408872"}]
         * categoryId : 169
         */

        public String imgUrl;
        public String dissid;
        public long createtime;
        public Object name;
        public String count;
        public String id;
        public int page;
        public String categoryId;
        public List<ItemsBean> items;

        public static class ItemsBean {
            /**
             * dissid : 7086552926
             * singermid : 000BUomX2AwnMH
             * singer : 织田裕二
             * songmid : 0001bnVH2RJHWx
             * albummid : 0002vI8P4FT0tC
             * albumid : 33059
             * songname : 君のいる朝
             * playUrl : null
             * albumname : ありがとう
             * imgUrl : https://y.gtimg.cn/music/photo_new/T002R90x90M0000002vI8P4FT0tC.jpg?max_age=2592000
             * mvUrl : null
             * size_flac : 0
             * media_mid : 002aSmzz25PAc8
             * id : 324349
             * songid : 408872
             */

            public String dissid;
            public String singermid;
            public String singer;
            public String songmid;
            public String albummid;
            public String albumid;
            public String songname;
            public Object playUrl;
            public String albumname;
            public String imgUrl;
            public Object mvUrl;
            public int size_flac;
            public String media_mid;
            public int id;
            public String songid;

        }
    }
}
