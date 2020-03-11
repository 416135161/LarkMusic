package internet.com.larkmusic.network.netnew;

import internet.com.larkmusic.BuildConfig;
import internet.com.larkmusic.network.netnew.bean.BaseRequest;
import internet.com.larkmusic.network.netnew.bean.BillBoardMusicListRequest;
import internet.com.larkmusic.network.netnew.bean.BillBoardResponse;
import internet.com.larkmusic.network.netnew.bean.BillBoardSongsResponse;
import internet.com.larkmusic.network.netnew.bean.GetAdsResponse;
import internet.com.larkmusic.network.netnew.bean.LrcResponse;
import internet.com.larkmusic.network.netnew.bean.NewListRequest;
import internet.com.larkmusic.network.netnew.bean.NewListResponse;
import internet.com.larkmusic.network.netnew.bean.PlayListRequest;
import internet.com.larkmusic.network.netnew.bean.PlayListResponse;
import internet.com.larkmusic.network.netnew.bean.PlayUrlRequest;
import internet.com.larkmusic.network.netnew.bean.PlayUrlResponse;
import internet.com.larkmusic.network.netnew.bean.SearchSingerResponse;
import internet.com.larkmusic.network.netnew.bean.SearchSongResponse;
import internet.com.larkmusic.network.netnew.bean.SingerSongsResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sjning
 * created on: 2019-07-26 11:18
 * description:
 */
public interface NewApi {
    String HOST = BuildConfig.DEBUG ? "http://39.100.157.223:8081/" : "http://39.100.157.223:80/";
    //    String HOST = "http://47.104.178.7:80/";
    String HOST_LRC = "https://c.y.qq.com/";
    String HOST_SINGER_SEARCH = "http://s.plcloud.music.qq.com/";
    String HOST_SEARCH = "http://soso.music.qq.com/";

    /**
     * 新歌
     *
     * @param request
     * @return
     */
    @POST("/android/newmusic/list.do")
    Call<NewListResponse> getNewMusicList(@Body NewListRequest request);

    /**
     * 播放地址
     *
     * @param request
     * @return
     */
    @POST("/android/play/music/getPlay.do")
    Call<PlayUrlResponse> getPlayUrl(@Body PlayUrlRequest request);

    /**
     * 歌词
     *
     * @param songmid
     * @return
     */
    @GET("/lyric/fcgi-bin/fcg_query_lyric_new.fcg?callback=MusicJsonCallback_lrc&pcachetime=1494070301711&g_tk=5381&jsonpCallback=MusicJsonCallback_lrc" +
            "&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&ice=0&platform=yqq&needNewCode=0")
    Call<LrcResponse> getLrc(@Query("songmid") String songmid);

    /**
     * 排行榜
     *
     * @param request
     * @return
     */
    @POST("/android/billBoard/list.do")
    Call<BillBoardResponse> getBillBoardList(@Body BaseRequest request);


    /**
     * 排行榜下面的歌曲
     *
     * @param request
     * @return
     */
    @POST("/android/billBoard/item/list.do")
    Call<BillBoardSongsResponse> getBillBoardMusicList(@Body BillBoardMusicListRequest request);

    /**
     * QQ搜歌
     *
     * @param keyword
     * @return
     */
    @GET("/fcgi-bin/client_search_cp?format=json&t=0&loginUin=0&inCharset=GB2312&outCharset=utf-8&qqmusic_guid" +
            "=b3788f8b88d8e81f1e0fb93ac52bd3f73aeccdd6&qqmusic_ver=50500&ct=6&catZhida=1&searchid=72ECCB3B-6341-4BAB-ADD0-BBD280484E5E10041" +
            "&flag_qc=0&remoteplace=txt.mac.search&new_json=1&lossless=0&aggr=0&cr=1&sem=0")
    Call<SearchSongResponse> searchSong(@Query("w") String keyword, @Query("p") int page, @Query("n") int pageSize);

    /**
     * 搜索歌手
     *
     * @param keyword
     * @return
     */
    @GET("/fcgi-bin/smartbox_new.fcg?utf8=1&is_xml=0")
    @Headers({"Referer:http://y.qq.com", "contentType:application/json,text/json,text/javascript,text/html,text/plain,application/x-javascript"})
    Call<SearchSingerResponse> searchSinger(@Query("key") String keyword);

    /**
     * 获取歌手下的歌曲
     *
     * @param keyword
     * @return
     */
    @GET("/v8/fcg-bin/fcg_v8_singer_track_cp.fcg?inCharset=utf-8&outCharset=utf-8&g_tk=5239908145&format&jsonp&platform=h5page&order=listen&from=h5&notice=0" +
            "&uin=0&needNewCode=1")
    Call<SingerSongsResponse> getSingerSongs(@Query("singerid") String keyword, @Query("begin") int begin, @Query("num") int pageSize);

    /**
     * 获取歌单
     *
     * @return
     */
    @POST("/android/musicList/list.do")
    Call<PlayListResponse> getPlayList(@Body PlayListRequest request);


    @GET("/adds/list.do")
    Call<GetAdsResponse> getAds(@Query("code") String code);

}
