package internet.com.larkmusic.network.netnew.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjning
 * created on: 2019-07-28 20:35
 * description:
 */
public class BillBoardSongsResponse implements Serializable {
    public String msg;
    public boolean success;
    public int rowCount;
    public List<BillBoardResponse.ItemsBean> result;
}
