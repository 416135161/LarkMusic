package internet.com.larkmusic.network.netnew;

import android.text.TextUtils;
import android.util.Base64;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import internet.com.larkmusic.action.ActionAlbumList;
import internet.com.larkmusic.action.ActionHotSongs;
import internet.com.larkmusic.action.ActionNewSongs;
import internet.com.larkmusic.action.ActionSearchSinger;
import internet.com.larkmusic.action.ActionSearchSongs;
import internet.com.larkmusic.action.ActionSingerSongs;
import internet.com.larkmusic.action.ActionStartLoading;
import internet.com.larkmusic.action.ActionStopLoading;
import internet.com.larkmusic.bean.Album;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.network.Config;
import internet.com.larkmusic.network.GetLrcCallBack;
import internet.com.larkmusic.network.GetSongCallBack;
import internet.com.larkmusic.network.HttpUtil;
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
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sjning
 * created on: 2019-07-26 13:05
 * description:
 */
public class NewCloudDataUtil {

    //获取新歌
    public static void getNewSongs(final int type, final String from) {
        NewListRequest newListRequest = new NewListRequest();
        if (from == Config.FROM_JAPAN) {
            newListRequest.type = NewListRequest.japan;
        } else {
            newListRequest.type = NewListRequest.us;
        }

        Call<NewListResponse> call = HttpUtil.getNewApi().getNewMusicList(newListRequest);
        call.enqueue(new Callback<NewListResponse>() {
            @Override
            public void onResponse(Call<NewListResponse> call, Response<NewListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null
                        && response.body().getResult().size() > 0) {
                    List<Song> songList = new ArrayList<>();
                    for (NewListResponse.ResultBean resultBean : response.body().getResult()) {
                        Song song = new Song();
                        song.setImgUrl(resultBean.getImgUrl());
                        song.setSongName(resultBean.getSongname());
                        song.setSingerName(resultBean.getSingername());
                        song.setHash(resultBean.getSongmid());
                        PlayUrlRequest playUrlRequest = new PlayUrlRequest();
                        playUrlRequest.songmid = resultBean.songmid;
                        playUrlRequest.songMediaId = resultBean.media_mid;
                        playUrlRequest.songName = resultBean.songname;
                        playUrlRequest.singermid = resultBean.singermid;
                        playUrlRequest.singername = resultBean.singername;
                        playUrlRequest.flac = resultBean.size_flac + "";
                        playUrlRequest.albummid = resultBean.albummid;
                        playUrlRequest.albumname = resultBean.albumname;
                        song.playUrlRequest = playUrlRequest;
                        songList.add(song);
                    }

                    ActionNewSongs action = new ActionNewSongs();
                    action.isOK = true;
                    action.trackList = songList;
                    action.type = type;
                    action.from = from;
                    EventBus.getDefault().post(action);

                } else {
                    onFailure(null, new Exception("is nothing"));
                }

            }

            @Override
            public void onFailure(Call<NewListResponse> call, Throwable t) {
                ActionNewSongs action = new ActionNewSongs();
                action.isOK = false;
                action.type = type;
                action.from = from;
                EventBus.getDefault().post(action);

            }

        });
    }

    //获取热歌
    public static void getBillBoard(final int type, final String from) {

        Call<BillBoardResponse> call = HttpUtil.getNewApi().getBillBoardList(new BaseRequest());
        call.enqueue(new Callback<BillBoardResponse>() {
            @Override
            public void onResponse(Call<BillBoardResponse> call, Response<BillBoardResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().result != null
                        && response.body().result.size() == 5) {
                    List<BillBoardResponse.ItemsBean> items;
                    if (TextUtils.equals(from, Config.FROM_JAPAN)) {
                        items = response.body().result.get(3).items;
                    } else {
                        items = response.body().result.get(0).items;
                    }
                    List<Song> songList = new ArrayList<>();
                    for (BillBoardResponse.ItemsBean resultBean : items) {
                        Song song = new Song();
                        song.setImgUrl(resultBean.imgUrl);
                        song.setSongName(resultBean.songname);
                        song.setSingerName(resultBean.singer);
                        song.setHash(resultBean.songmid);
                        PlayUrlRequest playUrlRequest = new PlayUrlRequest();
                        playUrlRequest.songmid = resultBean.songmid;
                        playUrlRequest.songMediaId = resultBean.media_mid;
                        playUrlRequest.songName = resultBean.songname;
                        playUrlRequest.singermid = resultBean.singermid;
                        playUrlRequest.singername = resultBean.singer;
                        playUrlRequest.flac = resultBean.size_flac + "";
                        playUrlRequest.albummid = resultBean.albummid;
                        playUrlRequest.albumname = resultBean.albumname;
                        song.playUrlRequest = playUrlRequest;
                        songList.add(song);
                    }

                    ActionHotSongs action = new ActionHotSongs();
                    action.isOK = true;
                    action.trackList = songList;
                    action.type = type;
                    action.from = from;
                    EventBus.getDefault().post(action);

                } else {
                    onFailure(null, new Exception("is nothing"));
                }

            }

            @Override
            public void onFailure(Call<BillBoardResponse> call, Throwable t) {
                ActionHotSongs action = new ActionHotSongs();
                action.isOK = false;
                action.type = type;
                action.from = from;
                EventBus.getDefault().post(action);

            }

        });
    }

    //获取歌曲详情
    public static void getSongPlayUrl(final Song song, final GetSongCallBack callBack) {
        if (song == null) {
            return;
        }
        EventBus.getDefault().post(new ActionStartLoading());
        Call<PlayUrlResponse> call = HttpUtil.getNewApi().getPlayUrl(song.playUrlRequest);
        call.enqueue(new Callback<PlayUrlResponse>() {

            @Override
            public void onResponse(Call<PlayUrlResponse> call, Response<PlayUrlResponse> response) {
                EventBus.getDefault().post(new ActionStopLoading());
                if (response.isSuccessful() && response.body() != null &&
                        !TextUtils.isEmpty(response.body().playUrl)) {
                    //对原来的歌曲对象赋值
                    song.setPlayUrl(response.body().getPlayUrl());
                    song.saveOrUpdate("hash = ?", song.getHash());
                    getLrc(song, null);
                    if (callBack != null) {
                        callBack.onSongGetOk(song);
                    }
                } else {
                    if (callBack != null) {
                        callBack.onSongGetFail();
                    }
                }

            }

            @Override
            public void onFailure(Call<PlayUrlResponse> call, Throwable t) {
                EventBus.getDefault().post(new ActionStopLoading());
                if (callBack != null) {
                    callBack.onSongGetFail();
                }
            }

        });
    }

    //获取歌词详情
    public static void getLrc(final Song song, final GetLrcCallBack callBack) {
        Call<LrcResponse> call = HttpUtil.getRetrofit(NewApi.HOST_LRC, new LrcInterceptor())
                .create(NewApi.class).getLrc(song.playUrlRequest.songmid);

        call.enqueue(new Callback<LrcResponse>() {

            @Override
            public void onResponse(Call<LrcResponse> call, Response<LrcResponse> response) {

                if (response.isSuccessful() && response.body() != null && !TextUtils.isEmpty(response.body().lyric)) {
                    //对原来的歌曲对象赋值
                    song.setLrc(new String(Base64.decode(response.body().lyric, Base64.NO_WRAP)));
                    song.saveOrUpdate("hash = ?", song.getHash());
                    if (callBack != null) {
                        callBack.onLrcGetOk(song);
                    }
                } else {
                    if (callBack != null) {
                        callBack.onLrcGetFail();
                    }
                }

            }

            @Override
            public void onFailure(Call<LrcResponse> call, Throwable t) {
                if (callBack != null) {
                    callBack.onLrcGetFail();
                }
            }

        });
    }

    //获取热歌
    public static void getBillBoardSongs(final int type, final String from, String rankId) {
        BillBoardMusicListRequest request = new BillBoardMusicListRequest();
        request.rankId = rankId;
        Call<BillBoardSongsResponse> call = HttpUtil.getNewApi().getBillBoardMusicList(request);
        call.enqueue(new Callback<BillBoardSongsResponse>() {
            @Override
            public void onResponse(Call<BillBoardSongsResponse> call, Response<BillBoardSongsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().result != null
                        && response.body().result.size() > 0) {
                    List<Song> songList = new ArrayList<>();
                    for (BillBoardResponse.ItemsBean resultBean : response.body().result) {
                        Song song = new Song();
                        song.setImgUrl(resultBean.imgUrl);
                        song.setSongName(resultBean.songname);
                        song.setSingerName(resultBean.singer);
                        song.setHash(resultBean.songmid);
                        PlayUrlRequest playUrlRequest = new PlayUrlRequest();
                        playUrlRequest.songmid = resultBean.songmid;
                        playUrlRequest.songMediaId = resultBean.media_mid;
                        playUrlRequest.songName = resultBean.songname;
                        playUrlRequest.singermid = resultBean.singermid;
                        playUrlRequest.singername = resultBean.singer;
                        playUrlRequest.flac = resultBean.size_flac + "";
                        playUrlRequest.albummid = resultBean.albummid;
                        playUrlRequest.albumname = resultBean.albumname;
                        song.playUrlRequest = playUrlRequest;
                        songList.add(song);
                    }

                    ActionHotSongs action = new ActionHotSongs();
                    action.isOK = true;
                    action.trackList = songList;
                    action.type = type;
                    action.from = from;
                    EventBus.getDefault().post(action);

                } else {
                    onFailure(null, new Exception("is nothing"));
                }

            }

            @Override
            public void onFailure(Call<BillBoardSongsResponse> call, Throwable t) {
                ActionHotSongs action = new ActionHotSongs();
                action.isOK = false;
                action.type = type;
                action.from = from;
                EventBus.getDefault().post(action);
            }

        });
    }

    /**
     * 搜歌
     *
     * @param key
     */
    public static void searchSongs(String key) {
        Call<SearchSongResponse> call = HttpUtil.getRetrofit(NewApi.HOST_LRC, null).create(NewApi.class).searchSong(key);

        call.enqueue(new Callback<SearchSongResponse>() {

            @Override
            public void onResponse(Call<SearchSongResponse> call, Response<SearchSongResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().data != null && response.body().data.song != null
                        && response.body().data.song.list != null && response.body().data.song.list.size() > 0) {
                    ArrayList<Song> songList = new ArrayList<>();
                    for (SearchSongResponse.DataBean.SongBean.ListBean listBean : response.body().data.song.list) {
                        Song song = new Song();
                        song.setImgUrl(listBean.album != null ? "https://y.gtimg.cn/music/photo_new/T002R90x90M000%@.jpg?max_age=2592000".replace("%@",
                                listBean.album.mid) : "");
                        song.setSongName(listBean.name);
                        song.setSingerName((listBean.singer != null && listBean.singer.size() > 0) ?
                                listBean.singer.get(0).name : "");
                        song.setHash(listBean.mid);
                        PlayUrlRequest playUrlRequest = new PlayUrlRequest();
                        playUrlRequest.songmid = listBean.mid;
                        playUrlRequest.songMediaId = listBean.file != null ? listBean.file.media_mid : "";
                        playUrlRequest.songName = listBean.name;
                        playUrlRequest.singermid = (listBean.singer != null && listBean.singer.size() > 0) ?
                                listBean.singer.get(0).mid : "";
                        playUrlRequest.singername = (listBean.singer != null && listBean.singer.size() > 0) ?
                                listBean.singer.get(0).name : "";
                        playUrlRequest.flac = "0";
                        playUrlRequest.albummid = listBean.album != null ? listBean.album.mid : "0";
                        playUrlRequest.albumname = listBean.album != null ? listBean.album.name : "";
                        song.playUrlRequest = playUrlRequest;
                        songList.add(song);
                    }

                    EventBus.getDefault().post(new ActionSearchSongs(songList));
                } else {
                    EventBus.getDefault().post(new ActionSearchSongs(null));
                }
            }

            @Override
            public void onFailure(Call<SearchSongResponse> call, Throwable t) {
                EventBus.getDefault().post(new ActionSearchSongs(null));
            }

        });

    }

    /**
     * 搜歌手
     *
     * @param key
     */
    public static void searchSinger(String key) {
        Call<SearchSingerResponse> call = HttpUtil.getRetrofit(NewApi.HOST_SINGER_SEARCH, new SingerInterceptor()).create(NewApi.class).searchSinger(key);

        call.enqueue(new Callback<SearchSingerResponse>() {

            @Override
            public void onResponse(Call<SearchSingerResponse> call, Response<SearchSingerResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().data != null && response.body().data.singer != null
                        && response.body().data.singer.itemlist != null && response.body().data.singer.itemlist.size() > 0) {
                    EventBus.getDefault().post(new ActionSearchSinger(response.body().data.singer.itemlist));
                } else {
                    EventBus.getDefault().post(new ActionSearchSinger(null));
                }
            }

            @Override
            public void onFailure(Call<SearchSingerResponse> call, Throwable t) {
                EventBus.getDefault().post(new ActionSearchSinger(null));
            }

        });

    }

    /**
     * 搜歌手
     *
     * @param singer
     */
    public static void getSingerSongs(final SearchSingerResponse.DataBean.SingerBean.Singer singer) {
        Call<SingerSongsResponse> call = HttpUtil.getRetrofit(NewApi.HOST_LRC, new SingerInterceptor()).create(NewApi.class).getSingerSongs(singer.id);

        call.enqueue(new Callback<SingerSongsResponse>() {

            @Override
            public void onResponse(Call<SingerSongsResponse> call, Response<SingerSongsResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().data != null && response.body().data.list != null
                        && response.body().data.list.size() > 0) {
                    ArrayList<Song> songList = new ArrayList<>();
                    for (SingerSongsResponse.DataBean.ListBean listBean : response.body().data.list) {
                        if (listBean.musicData == null) {
                            continue;
                        }
                        Song song = new Song();
                        song.setImgUrl(listBean.musicData.albummid != null ?
                                "https://y.gtimg.cn/music/photo_new/T002R90x90M000%@.jpg?max_age=2592000".replace("%@",
                                        listBean.musicData.albummid) : "");
                        song.setSongName(listBean.musicData.songname);
                        song.setSingerName((listBean.musicData.singer != null && listBean.musicData.singer.size() > 0) ?
                                listBean.musicData.singer.get(0).name : "");
                        song.setHash(listBean.musicData.songmid);
                        PlayUrlRequest playUrlRequest = new PlayUrlRequest();
                        playUrlRequest.songmid = listBean.musicData.songmid;
                        playUrlRequest.songMediaId = listBean.musicData.strMediaMid != null ? listBean.musicData.strMediaMid : "";
                        playUrlRequest.songName = listBean.musicData.songname;
                        playUrlRequest.singermid = (listBean.musicData.singer != null && listBean.musicData.singer.size() > 0) ?
                                listBean.musicData.singer.get(0).mid : "";
                        playUrlRequest.singername = (listBean.musicData.singer != null && listBean.musicData.singer.size() > 0) ?
                                listBean.musicData.singer.get(0).name : "";
                        playUrlRequest.flac = "0";
                        playUrlRequest.albummid = listBean.musicData.albummid != null ? listBean.musicData.albummid : "0";
                        playUrlRequest.albumname = listBean.musicData.albumname != null ? listBean.musicData.albumname : "";
                        song.playUrlRequest = playUrlRequest;
                        songList.add(song);
                    }

                    EventBus.getDefault().post(new ActionSingerSongs(songList, singer));
                } else {
                    EventBus.getDefault().post(new ActionSingerSongs(null));
                }
            }

            @Override
            public void onFailure(Call<SingerSongsResponse> call, Throwable t) {
                EventBus.getDefault().post(new ActionSingerSongs(null));
            }

        });

    }


    /**
     * 获取歌单
     *
     * @param type
     */
    public static void getPlayList(final String type) {
        PlayListRequest playListRequest = new PlayListRequest();
        playListRequest.type = type;
        Call<PlayListResponse> call = HttpUtil.getNewApi().getPlayList(playListRequest);

        call.enqueue(new Callback<PlayListResponse>() {

            @Override
            public void onResponse(Call<PlayListResponse> call, Response<PlayListResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().result != null && response.body().result.size() > 0) {
                    ArrayList<Album> albumList = new ArrayList<>();
                    for (PlayListResponse.ResultBean resultBean : response.body().result) {
                        if (resultBean.items == null || resultBean.items.size() == 0) {
                            continue;
                        }
                        Album album = new Album();
                        album.setImgUrl(resultBean.imgUrl != null ? resultBean.imgUrl.replace("600", "150") : "");
                        albumList.add(album);
                        ArrayList<Song> songList = new ArrayList<>();
                        for (int i = 0; i < resultBean.items.size(); i++) {
                            PlayListResponse.ResultBean.ItemsBean listBean = resultBean.items.get(i);
                            if (i == 0) {
                                album.setName(listBean.albumname);
                            }
                            Song song = new Song();
                            song.setImgUrl(listBean.albummid != null ?
                                    "https://y.gtimg.cn/music/photo_new/T002R90x90M000%@.jpg?max_age=2592000".replace("%@",
                                            listBean.albummid) : "");
                            song.setSongName(listBean.songname);
                            song.setSingerName(listBean.singer);
                            song.setHash(listBean.songmid);
                            PlayUrlRequest playUrlRequest = new PlayUrlRequest();
                            playUrlRequest.songmid = listBean.songmid;
                            playUrlRequest.songMediaId = listBean.media_mid;
                            playUrlRequest.songName = listBean.songname;
                            playUrlRequest.singermid = listBean.singermid;
                            playUrlRequest.singername = listBean.singer;
                            playUrlRequest.flac = "0";
                            playUrlRequest.albummid = listBean.albummid;
                            playUrlRequest.albumname = listBean.albumname;
                            song.playUrlRequest = playUrlRequest;
                            songList.add(song);
                        }
                        album.songList = songList;
                    }
                    EventBus.getDefault().post(new ActionAlbumList(albumList, type));
                } else {
                    EventBus.getDefault().post(new ActionAlbumList(null, null));
                }
            }

            @Override
            public void onFailure(Call<PlayListResponse> call, Throwable t) {
                EventBus.getDefault().post(new ActionAlbumList(null, null));
            }

        });

    }
}
