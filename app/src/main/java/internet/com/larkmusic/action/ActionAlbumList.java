package internet.com.larkmusic.action;

import java.util.ArrayList;

import internet.com.larkmusic.bean.Album;

/**
 * Created by sjning
 * created on: 2019/1/14 上午7:30
 * description:
 */
public class ActionAlbumList {
    public String from;
    public ArrayList<Album> albumList;

    public ActionAlbumList(ArrayList<Album> albumList, String from) {
        this.albumList = albumList;
        this.from = from;
    }

    public ArrayList<Album> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(ArrayList<Album> albumList) {
        this.albumList = albumList;
    }
}
