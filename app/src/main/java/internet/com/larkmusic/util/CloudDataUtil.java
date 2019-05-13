package internet.com.larkmusic.util;

import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import internet.com.larkmusic.action.ActionBrowPlayTeam;
import internet.com.larkmusic.action.ActionHotSongs;
import internet.com.larkmusic.action.ActionListPlayTeam;
import internet.com.larkmusic.action.ActionNewSongs;
import internet.com.larkmusic.action.ActionPlayList;
import internet.com.larkmusic.action.ActionStopLoading;
import internet.com.larkmusic.bean.Album;
import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.network.Config;
import internet.com.larkmusic.network.HttpUtil;
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
                        EventBus.getDefault().post(new ActionListPlayTeam(list, from));
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
                    EventBus.getDefault().post(new ActionListPlayTeam(null, null));
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
    public static void getSongFromCloud(String hash) {
        StreamService ss = HttpUtil.getApiService(Config.API_HOST, null);
        Call<Song> call = ss.getSongDetail(hash);
        call.enqueue(new Callback<Song>() {

            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {

                if (response.isSuccessful() && response.body() != null) {


                }

            }

            @Override
            public void onFailure(Call<Song> call, Throwable t) {
                EventBus.getDefault().post(new ActionStopLoading());

            }

        });
    }
}
