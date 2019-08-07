package internet.com.larkmusic.action;

import internet.com.larkmusic.network.netnew.bean.GetAdsResponse;

/**
 * Created by sjning
 * created on: 2019-08-07 14:18
 * description:
 */
public class ActionMyAds {

    public GetAdsResponse.ResultBean resultBean;

    public ActionMyAds(GetAdsResponse.ResultBean resultBean) {
        this.resultBean = resultBean;
    }
}
