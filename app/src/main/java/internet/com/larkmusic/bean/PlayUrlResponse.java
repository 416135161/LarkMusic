package internet.com.larkmusic.bean;

import java.io.Serializable;

/**
 * Created by sjning
 * created on: 2019-07-17 10:12
 * description:
 */
public class PlayUrlResponse implements Serializable {


    /**
     * success : true
     * playUrl : http://fs.ios.kugou.com/201907162003/21abedbd20cc0390e9883dfed9837bba/G028/M0A/0F/05/XA0DAFWjCsCAKghcAD32dJlvP4c723.mp3
     */

    private boolean success;
    private String playUrl;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }
}
