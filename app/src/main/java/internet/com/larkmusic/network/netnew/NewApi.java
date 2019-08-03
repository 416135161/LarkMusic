package internet.com.larkmusic.network.netnew;

import java.util.ArrayList;

import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.network.netnew.bean.BaseRequest;
import internet.com.larkmusic.network.netnew.bean.BillBoardMusicListRequest;
import internet.com.larkmusic.network.netnew.bean.BillBoardResponse;
import internet.com.larkmusic.network.netnew.bean.BillBoardSongsResponse;
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
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sjning
 * created on: 2019-07-26 11:18
 * description:
 */
public interface NewApi {
    String HOST = "http://39.100.157.223:80/";
//    String HOST = "http://47.104.178.7:80/";
    String HOST_LRC = "https://c.y.qq.com/";
    String HOST_SINGER_SEARCH = "http://s.plcloud.music.qq.com/";

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
    @GET("/soso/fcgi-bin/client_search_cp?ct=24&qqmusic_ver=1298&new_json=1&remoteplace=sizer.yqq" +
            ".song_next&searchid=&t=0&aggr=1&cr=1&catZhida=1&lossless=0&flag_qc=0&p=0&n=20&g_tk=&jsonpCallback=&loginUin=0&hostUin=0&format=json&inCharset" +
            "=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0")
    Call<SearchSongResponse> searchSong(@Query("w") String keyword);

    /**
     * 搜索歌手
     *
     * @param keyword
     * @return
     */
    @GET("/fcgi-bin/smartbox_new.fcg?utf8=1&is_xml=0")
    Call<SearchSingerResponse> searchSinger(@Query("key") String keyword);

    /**
     * 获取歌手下的歌曲
     *
     * @param keyword
     * @return
     */
    @GET("/v8/fcg-bin/fcg_v8_singer_track_cp.fcg?inCharset=utf-8&outCharset=utf-8&g_tk=5239908145&format&jsonp&platform=h5page&order=listen&from=h5&notice=0" +
            "&uin=0&needNewCode=1&begin=0&num=20")
    Call<SingerSongsResponse> getSingerSongs(@Query("singerid") String keyword);

    /**
     * 获取歌单
     *
     * @return
     */
    @POST("/android/musicList/list.do")
    Call<PlayListResponse> getPlayList(@Body PlayListRequest request);

}
