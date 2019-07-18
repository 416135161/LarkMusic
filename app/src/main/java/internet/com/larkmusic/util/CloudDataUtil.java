package internet.com.larkmusic.util;

import android.text.TextUtils;
import android.util.Base64;

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
import internet.com.larkmusic.bean.GetImageRequest;
import internet.com.larkmusic.bean.GetImageResponse;
import internet.com.larkmusic.bean.GetLrcResponse;
import internet.com.larkmusic.bean.PlayUrlResponse;
import internet.com.larkmusic.bean.SearchLrcResponse;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.bean.songDetailResponse.SongDetailKuGou;
import internet.com.larkmusic.network.Config;
import internet.com.larkmusic.network.DetailInterceptor;
import internet.com.larkmusic.network.GetLrcCallBack;
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
        StreamService ss = HttpUtil.getApiService(Config.HOST_GET_SONG, new DetailInterceptor());
        Call<SongDetailKuGou> call = ss.getSongDetailKuGou(song.getHash());
        call.enqueue(new Callback<SongDetailKuGou>() {

            @Override
            public void onResponse(Call<SongDetailKuGou> call, Response<SongDetailKuGou> response) {
                EventBus.getDefault().post(new ActionStopLoading());
                if (response.isSuccessful() && response.body() != null && response.body().getErr_code() == 0) {
                    Song data = TransformUtil.detailResponse2Song(response.body());
                    //对原来的歌曲对象赋值
                    song.setPlayUrl(data.getPlayUrl());
                    song.setLrc(data.getLrc());
                    song.setPortrait(data.getPortrait());
                    song.setImgUrl(data.getImgUrl());
                    if (callBack != null) {
                        callBack.onSongGetOk(data);
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

    //获取歌曲详情
    public static void getSongPlayUrl(final Song song, final GetSongCallBack callBack) {
        if (song == null) {
            return;
        }
        EventBus.getDefault().post(new ActionStartLoading());
        StreamService ss = HttpUtil.getApiService(Config.HOST_GET_PLAY_URL, null);
        Call<PlayUrlResponse> call = ss.getPlayUrl(song.getHash());
        call.enqueue(new Callback<PlayUrlResponse>() {

            @Override
            public void onResponse(Call<PlayUrlResponse> call, Response<PlayUrlResponse> response) {
                EventBus.getDefault().post(new ActionStopLoading());
                if (response.isSuccessful() && response.body() != null && !TextUtils.isEmpty(response.body().getPlayUrl())
                        && response.body().getPlayUrl().length() > 4) {
                    //对原来的歌曲对象赋值
                    song.setPlayUrl(response.body().getPlayUrl());
                    song.saveOrUpdate("hash = ?", song.getHash());
                    if (TextUtils.isEmpty(song.getImgUrl())) {
                        getSongImage(song);
                    }
                    if (TextUtils.isEmpty(song.getLrc())) {
                        getSongLrc(song, null);
                    }
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

    //获取歌曲详情
    public static void getSongLrc(final Song song, final GetLrcCallBack callBack) {
        if (song == null) {
            return;
        }

        StreamService ss = HttpUtil.getApiService(Config.HOST_GET_LRC, null);
        Call<SearchLrcResponse> call = ss.searchLrc(song.getHash());
        call.enqueue(new Callback<SearchLrcResponse>() {

            @Override
            public void onResponse(Call<SearchLrcResponse> call, Response<SearchLrcResponse> response) {

                if (response.isSuccessful() && response.body() != null && response.body().getCandidates() != null
                        && response.body().getCandidates().size() > 0) {
                    SearchLrcResponse.CandidatesBean candidatesBean = response.body().getCandidates().get(0);
                    getLrc(song, candidatesBean.getAccesskey(), candidatesBean.getId(), callBack);

                } else {
                    if (callBack != null) {
                        callBack.onLrcGetFail();
                    }
                }

            }

            @Override
            public void onFailure(Call<SearchLrcResponse> call, Throwable t) {
                if (callBack != null) {
                    callBack.onLrcGetFail();
                }
            }

        });
    }

    private static void getLrc(final Song song, String accesskey, String id, final GetLrcCallBack callBack) {
        StreamService ss = HttpUtil.getApiService(Config.HOST_GET_LRC, null);
        Call<GetLrcResponse> call = ss.getLrc(accesskey, id);
        call.enqueue(new Callback<GetLrcResponse>() {

            @Override
            public void onResponse(Call<GetLrcResponse> call, Response<GetLrcResponse> response) {

                if (response.isSuccessful() && response.body() != null && !TextUtils.isEmpty(response.body().getContent())) {
                    //对原来的歌曲对象赋值
                    song.setLrc(new String(Base64.decode(response.body().getContent(), Base64.NO_WRAP)));
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
            public void onFailure(Call<GetLrcResponse> call, Throwable t) {
                if (callBack != null) {
                    callBack.onLrcGetFail();
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

    //获取歌曲详情
    public static void getSongImage(final Song song) {
        if (song == null) {
            return;
        }
        StreamService ss = HttpUtil.getApiService(Config.HOST_GET_IMAGE, null);
        GetImageRequest content = new GetImageRequest();
        content.getData().add(new GetImageRequest.DataBean(song.getHash()));
        Call<GetImageResponse> call = ss.getSongImg(content);
        call.enqueue(new Callback<GetImageResponse>() {

            @Override
            public void onResponse(Call<GetImageResponse> call, Response<GetImageResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null
                        && response.body().getData().size() > 0) {
                    //对原来的歌曲对象赋值
                    if (response.body().getData().get(0) != null
                            && response.body().getData().get(0).size() > 0
                            && !TextUtils.isEmpty(response.body().getData().get(0).get(0).getSizable_cover())) {
                        song.setImgUrl(response.body().getData().get(0).get(0).getSizable_cover().replace("{size}", "400"));
                        song.saveOrUpdate("hash = ?", song.getHash());
                    }

                }
            }

            @Override
            public void onFailure(Call<GetImageResponse> call, Throwable t) {

            }

        });
    }

}
