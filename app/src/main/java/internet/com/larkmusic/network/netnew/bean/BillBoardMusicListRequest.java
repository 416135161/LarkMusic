package internet.com.larkmusic.network.netnew.bean;

/**
 * Created by sjning
 * created on: 2019-07-28 11:02
 * description:
 */
public class BillBoardMusicListRequest extends BaseRequest {

    public static final String RANK_Europe_US = "3";
    public static final String RANK_BillBoard = "108";
    public static final String RANK_iTunes = "123";
    public static final String RANK_Japan_TOP = "17";
    public static final String RANK_Japan_Public = "105";


    public String start = "0";
    public String pageSize = "60";
    public String rankId;
}
