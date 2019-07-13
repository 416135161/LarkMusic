package internet.com.larkmusic.network;

import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2019/1/9 下午7:34
 * description:
 */
public interface GetSongCallBack {
    void onSongGetOk(Song song);

    void onSongGetFail();
}
