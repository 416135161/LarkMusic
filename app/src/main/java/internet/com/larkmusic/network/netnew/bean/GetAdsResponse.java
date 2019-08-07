package internet.com.larkmusic.network.netnew.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjning
 * created on: 2019-08-07 11:58
 * description:
 */
public class GetAdsResponse implements Serializable {


    /**
     * msg : network error
     * result : [{"code":"candyMusic","icon":"videoimages/5555.png","id":28,"title":"candyMusic","url":"https://apps.apple.com/jp/app/id1465874743",
     * "content":"https://apps.apple.com/jp/app/id1465874743"}]
     * success : true
     * rowCount : 1
     */

    public String msg;
    public boolean success;
    public int rowCount;
    public List<ResultBean> result;


    public static class ResultBean implements Serializable{
        /**
         * code : candyMusic
         * icon : videoimages/5555.png
         * id : 28
         * title : candyMusic
         * url : https://apps.apple.com/jp/app/id1465874743
         * content : https://apps.apple.com/jp/app/id1465874743
         */

        public String code;
        public String icon;
        public int id;
        public String title;
        public String url;
        public String content;

    }
}
