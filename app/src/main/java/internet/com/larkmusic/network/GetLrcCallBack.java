package internet.com.larkmusic.network;

import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2019-07-17 16:03
 * description:
 */
public interface GetLrcCallBack {

    void onLrcGetOk(Song song);

    void onLrcGetFail();
}
