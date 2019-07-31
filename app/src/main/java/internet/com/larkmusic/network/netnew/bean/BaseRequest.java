package internet.com.larkmusic.network.netnew.bean;

import java.io.Serializable;

import internet.com.larkmusic.app.MusicApplication;

/**
 * Created by sjning
 * created on: 2019-07-26 12:55
 * description:
 */
public class BaseRequest implements Serializable {
    public String appid = MusicApplication.getInstance().getPackageName();

}
