package internet.com.larkmusic.network.netnew;

import internet.com.larkmusic.network.netnew.bean.BaseRequest;
import internet.com.larkmusic.network.netnew.bean.BillBoardResponse;
import internet.com.larkmusic.network.netnew.bean.LrcResponse;
import internet.com.larkmusic.network.netnew.bean.NewListRequest;
import internet.com.larkmusic.network.netnew.bean.NewListResponse;
import internet.com.larkmusic.network.netnew.bean.PlayUrlRequest;
import internet.com.larkmusic.network.netnew.bean.PlayUrlResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sjning
 * created on: 2019-07-26 11:18
 * description:
 */
public interface NewApi {

    String HOST = "http://47.104.178.7:80/";
    String HOST_LRC = "https://c.y.qq.com/";

    /**
     * 新歌
     * @param request
     * @return
     */
    @POST("/android/newmusic/list.do")
    Call<NewListResponse> getNewMusicList(@Body NewListRequest request);

    /**
     * 播放地址
     * @param request
     * @return
     */
    @POST("/android/play/music/getPlay.do")
    Call<PlayUrlResponse> getPlayUrl(@Body PlayUrlRequest request);

    /**
     * 歌词
     * @param songmid
     * @return
     */
    @GET("/lyric/fcgi-bin/fcg_query_lyric_new.fcg?callback=MusicJsonCallback_lrc&pcachetime=1494070301711&g_tk=5381&jsonpCallback=MusicJsonCallback_lrc&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&ice=0&platform=yqq&needNewCode=0")
    Call<LrcResponse> getLrc(@Query("songmid")String songmid);

    /**
     * 新歌
     * @param request
     * @return
     */
    @POST("/android/billBoard/list.do")
    Call<BillBoardResponse> getBillBoardList(@Body BaseRequest request);


}
