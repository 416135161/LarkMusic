package internet.com.larkmusic.network.netnew.bean;

import java.io.Serializable;

/**
 * Created by sjning
 * created on: 2019-07-26 12:57
 * description:
 */
public class NewListRequest extends BaseRequest implements Serializable {
    public String type = "4";
    public String start = "0";
    public String pageSize = "40";
}
