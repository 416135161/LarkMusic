package internet.com.larkmusic.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2019-06-27 15:05
 * description:
 */
public class RecentSongService {
    public static final int MAX_SIZE = 30;

    private static RecentSongService recentSongService;

    private RecentSongService() {

    }

    public static RecentSongService getInstance() {
        if (recentSongService == null) {
            recentSongService = new RecentSongService();
        }
        return recentSongService;
    }

    public List<Song> getSongList() {
        if (SpHelper.recentSongs == null) {
            Gson gs = new Gson();
            SpHelper.recentSongs = gs.fromJson(SpHelper.getDefault().getDefault().getString(SpHelper.KEY_RECENT_SONGS, "[]"), new TypeToken<List<Song>>() {
            }.getType());
        }
        return SpHelper.recentSongs;
    }

    public void saveSong(Song song) {
        if (song == null) {
            return;
        }

        if (SpHelper.recentSongs == null) {
            getSongList();
        }

        for (int i = 0; i < SpHelper.recentSongs.size(); i++) {
            Song item = SpHelper.recentSongs.get(i);
            //如果已保存，则返回
            if (TextUtils.equals(item.getHash(), song.getHash())) {
                SpHelper.recentSongs.remove(i);
                break;
            }
        }
        SpHelper.recentSongs.add(0, cloneSong(song));
        if (SpHelper.recentSongs.size() > MAX_SIZE) {
            SpHelper.recentSongs = SpHelper.recentSongs.subList(0, MAX_SIZE);
        }
        SpHelper.getDefault().putString(SpHelper.KEY_RECENT_SONGS, new Gson().toJson(SpHelper.recentSongs));

    }

    public void deleteSong(Song song) {
        for (int i = 0; i < SpHelper.recentSongs.size(); i++) {
            Song item = SpHelper.recentSongs.get(i);
            //如果已保存则，从保存中删除
            if (TextUtils.equals(item.getHash(), song.getHash())) {
                SpHelper.recentSongs.remove(i);
                SpHelper.getDefault().putString(SpHelper.KEY_RECENT_SONGS, new Gson().toJson(SpHelper.recentSongs));
                return;
            }
        }
    }

    private Song cloneSong(Song song) {
        Song newSong = new Song();
        newSong.setSingerName(song.getSingerName());
        newSong.setPortrait(song.getPortrait());
        newSong.setImgUrl(song.getImgUrl());
        newSong.setHash(song.getHash());
        newSong.setSongName(song.getSongName());
        newSong.playUrlRequest = song.playUrlRequest;
        return newSong;
    }

}
