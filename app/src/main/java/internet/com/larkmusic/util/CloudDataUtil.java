package internet.com.larkmusic.util;

import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import internet.com.larkmusic.action.ActionAlbumList;
import internet.com.larkmusic.action.ActionBrowPlayTeam;
import internet.com.larkmusic.action.ActionHotSongs;
import internet.com.larkmusic.action.ActionNewSongs;
import internet.com.larkmusic.action.ActionPlayList;
import internet.com.larkmusic.action.ActionSearchSongs;
import internet.com.larkmusic.action.ActionStartLoading;
import internet.com.larkmusic.action.ActionStopLoading;
import internet.com.larkmusic.bean.Album;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.bean.songDetailResponse.SongDetailKuGou;
import internet.com.larkmusic.network.Config;
import internet.com.larkmusic.network.DetailIntercepter;
import internet.com.larkmusic.network.GetSongCallBack;
import internet.com.larkmusic.network.HttpUtil;
import internet.com.larkmusic.network.QueryInterceptor;
import internet.com.larkmusic.network.StreamService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sjning
 * created on: 2019/1/12 上午10:09
 * description:
 */
public final class CloudDataUtil {

    //获取专辑列表
    public static void getPlayTeamList(int pageSize, final int type, final String from) {
        StreamService ss = HttpUtil.getApiService(Config.API_HOST, null);
        Call<ArrayList<Album>> call;
        if (TextUtils.equals(from, Config.FROM_US) || TextUtils.equals(from, Config.FROM_JAPAN)) {
            call = ss.getPlayTeamList(pageSize, from);
        } else {
            call = ss.getEuropePlayTeamList(pageSize, from);
        }
        call.enqueue(new Callback<ArrayList<Album>>() {

            @Override
            public void onResponse(Call<ArrayList<Album>> call, Response<ArrayList<Album>> response) {
                if (response.isSuccessful() && response.body() != null
                        && !response.body().isEmpty()) {
                    ArrayList<Album> list = response.body();
                    if (type == ActionBrowPlayTeam.TYPE_TEAM_LIST) {
                        EventBus.getDefault().post(new ActionAlbumList(list, from));
                    } else if (type == ActionBrowPlayTeam.TYPE_BROW) {
                        EventBus.getDefault().post(new ActionBrowPlayTeam(list));

                    }
                } else {
                    onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Album>> call, Throwable t) {
                if (type == ActionBrowPlayTeam.TYPE_TEAM_LIST) {
                    EventBus.getDefault().post(new ActionAlbumList(null, null));
                } else if (type == ActionBrowPlayTeam.TYPE_BROW) {
                    EventBus.getDefault().post(new ActionBrowPlayTeam(null));
                }
            }

        });
    }

    //获取专辑下的歌曲
    public static void getPlayList(String id, String from) {
        StreamService ss = HttpUtil.getApiService(Config.API_HOST, null);
        Call<ArrayList<Song>> call;
        if (TextUtils.equals(from, Config.FROM_US) || TextUtils.equals(from, Config.FROM_JAPAN)) {
            call = ss.getPlayList(id);
        } else {
            call = ss.getEuropePlayList(id);
        }

        call.enqueue(new Callback<ArrayList<Song>>() {

            @Override
            public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                if (response.isSuccessful() && response.body() != null
                        && !response.body().isEmpty()) {
                    ArrayList<Song> list = response.body();
                    EventBus.getDefault().post(new ActionPlayList(list));
                } else {
                    onFailure(null, new Exception("is nothing"));
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Song>> call, Throwable t) {
                ActionPlayList action = new ActionPlayList(null);
                action.isOK = false;
                EventBus.getDefault().post(action);

            }

        });
    }


    //获取新歌
    public static void getNewSongs(final int type, final String from) {
        StreamService ss = HttpUtil.getApiService(Config.API_HOST, null);
        Call<ArrayList<Song>> call = ss.getNewSongs(from);
        call.enqueue(new Callback<ArrayList<Song>>() {
            @Override
            public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ActionNewSongs action = new ActionNewSongs();
                    action.isOK = true;
                    action.trackList = response.body();
                    action.type = type;
                    action.from = from;
                    EventBus.getDefault().post(action);
                } else {
                    onFailure(null, new Exception("is nothing"));
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Song>> call, Throwable t) {
                ActionNewSongs action = new ActionNewSongs();
                action.isOK = false;
                action.type = type;
                action.from = from;
                EventBus.getDefault().post(action);

            }

        });
    }

    //获取热歌
    public static void getHotSongs(final int type, final String from) {
        StreamService ss = HttpUtil.getApiService(Config.API_HOST, null);
        Call<ArrayList<Song>> call = ss.getBillSongs(from);
        call.enqueue(new Callback<ArrayList<Song>>() {
            @Override
            public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    ActionHotSongs action = new ActionHotSongs();
                    action.isOK = true;
                    action.trackList = response.body();
                    action.type = type;
                    action.from = from;
                    EventBus.getDefault().post(action);
                } else {
                    onFailure(null, new Exception("is nothing"));
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Song>> call, Throwable t) {
                ActionHotSongs action = new ActionHotSongs();
                action.isOK = false;
                action.type = type;
                action.from = from;
                EventBus.getDefault().post(action);

            }

        });
    }

    //获取歌曲详情
    public static void getSongFromCloud(final Song song, final GetSongCallBack callBack) {
        if (song == null) {
            return;
        }
        EventBus.getDefault().post(new ActionStartLoading());
        StreamService ss = HttpUtil.getApiService(Config.HOST_GET_SONG, new DetailIntercepter());
        Call<SongDetailKuGou> call = ss.getSongDetailKuGou(song.getHash());
        call.enqueue(new Callback<SongDetailKuGou>() {

            @Override
            public void onResponse(Call<SongDetailKuGou> call, Response<SongDetailKuGou> response) {
                EventBus.getDefault().post(new ActionStopLoading());
                if (response.isSuccessful() && response.body() != null) {
                    Song data = TransformUtil.detailResponse2Song(response.body());
                    song.setImgUrl(data.getImgUrl());
                    song.setPlayUrl(data.getPlayUrl());
                    song.setDuration(data.getDuration());
                    song.setLrc(data.getLrc());
                    song.setPortrait(data.getPortrait());
                    if (callBack != null) {
                        callBack.onSongGetOk();
                    }
                    CloudDataUtil.saveSongImg(data.getHash(), data.getImgUrl());
                } else {
                    if (callBack != null) {
                        callBack.onSongGetFail();
                    }
                }

            }

            @Override
            public void onFailure(Call<SongDetailKuGou> call, Throwable t) {
                EventBus.getDefault().post(new ActionStopLoading());
                if (callBack != null) {
                    callBack.onSongGetFail();
                }
            }

        });
    }

    public static void searchSongs(String key) {
        StreamService ss = HttpUtil.getApiService(Config.API_HOST, new QueryInterceptor());
        Call<ArrayList<Song>> call = ss.searchSong(key, Config.SEARCH_COUNT);
        call.enqueue(new Callback<ArrayList<Song>>() {

            @Override
            public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                if (response.isSuccessful()) {
                    EventBus.getDefault().post(new ActionSearchSongs(response.body()));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Song>> call, Throwable t) {
                EventBus.getDefault().post(new ActionSearchSongs(null));
            }

        });

    }


    //保存图片到远程服务器
    public static void saveSongImg(String hash, String img) {
        StreamService ss = HttpUtil.getApiService(Config.API_HOST, null);
        Call<Boolean> call = ss.saveSongImg(hash, img);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
            }

        });
    }
}
