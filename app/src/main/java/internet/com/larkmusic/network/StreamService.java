package internet.com.larkmusic.network;

import java.util.ArrayList;

import internet.com.larkmusic.bean.Album;
import internet.com.larkmusic.bean.Song;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sjning
 * created on: 2019/5/12 下午9:12
 * description:
 */
public interface StreamService {
    /**
     * 这是获取新歌的  size弄成240
     *
     * @return
     */
    @GET("/music/new")
    Call<ArrayList<Song>> getNewSongs(@Query("from") String from);

    /**
     * 这是获取排行榜数据的
     *
     * @return
     */
    @GET("/music/hot")
    Call<ArrayList<Song>> getBillSongs(@Query("from") String from);

    /**
     * 酷狗搜歌
     *
     * @param keyword
     * @return
     */
    @GET("/music/search")
    Call<ArrayList<Song>> searchSong(@Query("keyword") String keyword, @Query("pageSize") int pageSize);

    /**
     * 获取歌曲播放地址
     *
     * @param hash
     * @return
     */
    @GET("/music/detail")
    Call<Song> getSongDetail(@Query("hash") String hash);

    /**
     * 获取歌单
     *
     * @param pageSize
     * @return
     */
    @GET("/music/albums")
    Call<ArrayList<Album>> getPlayTeamList(@Query("pageSize") int pageSize, @Query("from") String from);

    /**
     * 获取歌单下的歌曲
     *
     * @param id
     * @return
     */
    @GET("/music/album/songs")
    Call<ArrayList<Song>> getPlayList(@Query("id") String id);

    /**
     * 获取欧洲歌单
     *
     * @param pageSize
     * @return
     */
    @GET("/music/europe/albums")
    Call<ArrayList<Album>> getEuropePlayTeamList(@Query("pageSize") int pageSize, @Query("from") String from);

    /**
     * 获取欧洲歌单下的歌曲
     *
     * @param id
     * @return
     */
    @GET("/music/europe/album/songs")
    Call<ArrayList<Song>> getEuropePlayList(@Query("id") String id);

}
