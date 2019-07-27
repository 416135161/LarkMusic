package internet.com.larkmusic.network.netnew;

import android.text.TextUtils;
import android.util.Base64;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import internet.com.larkmusic.action.ActionHotSongs;
import internet.com.larkmusic.action.ActionNewSongs;
import internet.com.larkmusic.action.ActionStartLoading;
import internet.com.larkmusic.action.ActionStopLoading;

import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.network.Config;
import internet.com.larkmusic.network.GetLrcCallBack;
import internet.com.larkmusic.network.GetSongCallBack;
import internet.com.larkmusic.network.HttpUtil;
import internet.com.larkmusic.network.netnew.bean.BaseRequest;
import internet.com.larkmusic.network.netnew.bean.BillBoardResponse;
import internet.com.larkmusic.network.netnew.bean.LrcResponse;
import internet.com.larkmusic.network.netnew.bean.NewListRequest;
import internet.com.larkmusic.network.netnew.bean.NewListResponse;
import internet.com.larkmusic.network.netnew.bean.PlayUrlRequest;
import internet.com.larkmusic.network.netnew.bean.PlayUrlResponse;
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

        Call<NewListResponse> call = HttpUtil.getNewApi().getNewMusicList(new NewListRequest());
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
    public static void getHotSongs(final int type, final String from) {

        Call<BillBoardResponse> call = HttpUtil.getNewApi().getBillBoardList(new BaseRequest());
        call.enqueue(new Callback<BillBoardResponse>() {
            @Override
            public void onResponse(Call<BillBoardResponse> call, Response<BillBoardResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().result != null
                        && response.body().result.size() == 5) {
                    List<BillBoardResponse.ItemsBean> items;
                    if(TextUtils.equals(from , Config.FROM_JAPAN)){
                        items = response.body().result.get(0).items;
                    }else {
                        items = response.body().result.get(4).items;
                    }
                    List<Song> songList = new ArrayList<>();
                    for (BillBoardResponse.ItemsBean resultBean : items) {
                        Song song = new Song();
                        song.setImgUrl(resultBean.imgUrl);
                        song.setSongName(resultBean.songname);
                        song.setSingerName(resultBean.singer);
                        song.setHash(resultBean.singermid);
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
                    Collections.shuffle(songList);
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
}
