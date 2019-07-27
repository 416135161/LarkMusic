package internet.com.larkmusic.network.netnew.bean;

import java.io.Serializable;

/**
 * Created by sjning
 * created on: 2019-07-26 15:17
 * description:
 */
public class PlayUrlResponse implements Serializable {


    /**
     * msg : network error
     * success : true
     * type : qq
     * playUrl : http://isure.stream.qqmusic.qq.com/C400002zQIbX3PXrd7
     * .m4a?vkey=9AA8819B1D664F442A2E6EB6C670E5354D8B4DF24E7EA4954498DD3155E0252DBB8D46DB288BCF08A49FEB6B94DCC9C62D7CF098FAA41474&guid
     * =1f51ef454af13c18edaf5563917ffddda17d915e&uin=1152921504615029873&fromtag=151
     */

    public String msg;
    public boolean success;
    public String type;
    public String playUrl;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }
}
