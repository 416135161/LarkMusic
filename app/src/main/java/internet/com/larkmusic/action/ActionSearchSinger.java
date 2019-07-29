package internet.com.larkmusic.action;

import java.util.List;

import internet.com.larkmusic.network.netnew.bean.SearchSingerResponse;

/**
 * Created by sjning
 * created on: 2019-07-29 11:15
 * description:
 */
public class ActionSearchSinger {
   public  List<SearchSingerResponse.DataBean.SingerBean.Singer> singerList;

    public ActionSearchSinger(List<SearchSingerResponse.DataBean.SingerBean.Singer> singerList) {
        this.singerList = singerList;
    }
}
