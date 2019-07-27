package internet.com.larkmusic.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2019/6/10 上午10:59
 * description:
 */
public class FavoriteService {
    public static final int MAX_SIZE = 30;

    private static FavoriteService favoriteService;

    private FavoriteService() {

    }

    public static FavoriteService getInstance() {
        if (favoriteService == null) {
            favoriteService = new FavoriteService();
        }
        return favoriteService;
    }

    public List<Song> getSongList() {
        if (SpHelper.favoriteSongs == null) {
            Gson gs = new Gson();
            SpHelper.favoriteSongs = gs.fromJson(SpHelper.getDefault().getDefault().getString(SpHelper.KEY_FAVORITE_SONGS, "[]"), new TypeToken<List<Song>>() {
            }.getType());
        }
        return SpHelper.favoriteSongs;
    }

    public void saveSong(Song song, boolean isFavorite) {
        if (SpHelper.favoriteSongs == null || song == null) {
            return;
        }
        // 加入我喜欢的
        if (isFavorite) {
            for (int i = 0; i < SpHelper.favoriteSongs.size(); i++) {
                Song item = SpHelper.favoriteSongs.get(i);
                //如果已保存，则返回
                if (TextUtils.equals(item.getHash(), song.getHash())) {
                    return;
                }
            }
            SpHelper.favoriteSongs.add(0, cloneSong(song));
            if (SpHelper.favoriteSongs.size() > MAX_SIZE) {
                SpHelper.favoriteSongs = SpHelper.favoriteSongs.subList(0, MAX_SIZE);
            }
            SpHelper.getDefault().putString(SpHelper.KEY_FAVORITE_SONGS, new Gson().toJson(SpHelper.favoriteSongs));
        } else {
            for (int i = 0; i < SpHelper.favoriteSongs.size(); i++) {
                Song item = SpHelper.favoriteSongs.get(i);
                //如果已保存则，从保存中删除
                if (TextUtils.equals(item.getHash(), song.getHash())) {
                    SpHelper.favoriteSongs.remove(i);
                    SpHelper.getDefault().putString(SpHelper.KEY_FAVORITE_SONGS, new Gson().toJson(SpHelper.favoriteSongs));
                    return;
                }
            }
        }
    }


    public boolean isFavorite(Song song) {
        if (SpHelper.favoriteSongs == null || song == null) {
            return false;
        }
        for (int i = 0; i < SpHelper.favoriteSongs.size(); i++) {
            Song item = SpHelper.favoriteSongs.get(i);
            //如果已保存，则返回
            if (TextUtils.equals(item.getHash(), song.getHash())) {
                return true;
            }
        }
        return false;
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
