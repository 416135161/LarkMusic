package internet.com.larkmusic.network.netnew.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjning
 * created on: 2019-07-26 21:21
 * description:
 */
public class BillBoardResponse implements Serializable {

    public String msg;
    public boolean success;
    public int rowCount;
    public List<BillBoardBean> result;


    public static class BillBoardBean implements Serializable {

        /**
         * imgUrl : null
         * rankid : 3
         * rankname : Europe and US TOP
         * id : 1
         * sort : 1
         * items : [{"singermid":"0034MZS446AF7O","singer":"Billie Eilish","songmid":"000AHdHp1WuDoZ","albummid":"002FN1rR4PKMng","albumid":"7159902",
         * "songname":"bad guy","playUrl":null,"albumname":"bad guy","imgUrl":"https://y.gtimg.cn/music/photo_new/T002R90x90M000002FN1rR4PKMng
         * .jpg?max_age=2592000","mvUrl":null,"size_flac":0,"media_mid":"000AHdHp1WuDoZ","rankid":"3","passtime":1564136468000,"id":2190,"page":0,"songid
         * ":"234396009"}]
         */

        public Object imgUrl;
        public String rankid;
        public String rankname;
        public int id;
        public int sort;
        public List<ItemsBean> items;

    }

    public static class ItemsBean implements Serializable {
        /**
         * singermid : 0034MZS446AF7O
         * singer : Billie Eilish
         * songmid : 000AHdHp1WuDoZ
         * albummid : 002FN1rR4PKMng
         * albumid : 7159902
         * songname : bad guy
         * playUrl : null
         * albumname : bad guy
         * imgUrl : https://y.gtimg.cn/music/photo_new/T002R90x90M000002FN1rR4PKMng.jpg?max_age=2592000
         * mvUrl : null
         * size_flac : 0
         * media_mid : 000AHdHp1WuDoZ
         * rankid : 3
         * passtime : 1564136468000
         * id : 2190
         * page : 0
         * songid : 234396009
         */

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
        public String rankid;
        public long passtime;
        public int id;
        public int page;
        public String songid;

    }
}
