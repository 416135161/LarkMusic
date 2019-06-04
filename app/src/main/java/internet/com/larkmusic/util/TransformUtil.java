package internet.com.larkmusic.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import internet.com.larkmusic.bean.Song;
import internet.com.larkmusic.bean.songDetailResponse.Authors;
import internet.com.larkmusic.bean.songDetailResponse.Data;
import internet.com.larkmusic.bean.songDetailResponse.SongDetailKuGou;

/**
 * Created by sjning
 * created on: 2019/1/9 下午4:03
 * description:
 */
public final class TransformUtil {
    private TransformUtil() {

    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public synchronized static Song detailResponse2Song(SongDetailKuGou songDetailBean) {

        Song song = new Song();
        if (songDetailBean != null && songDetailBean.getData() != null) {
            Data data = songDetailBean.getData();
            song.setSongName(data.getSong_name());
            song.setHash(data.getHash());
            song.setSingerName(data.getAuthor_name());
            song.setDuration(data.getTimelength() / 1000);
            song.setPlayUrl(data.getPlay_url());
            song.setLrc(data.getLyrics());
            song.setImgUrl(data.getImg());
            if (data.getAuthors() != null && data.getAuthors().size() > 0) {
                Authors authors = data.getAuthors().get(0);
                song.setPortrait(authors.getAvatar());
            }
        }
        return song;
    }
}
