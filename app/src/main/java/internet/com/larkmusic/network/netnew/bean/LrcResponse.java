package internet.com.larkmusic.network.netnew.bean;

import java.io.Serializable;

/**
 * Created by sjning
 * created on: 2019-07-26 16:44
 * description:
 */
public class LrcResponse implements Serializable {


    /**
     * retcode : 0
     * code : 0
     * subcode : 0
     * lyric :
     */

    public int retcode;
    public int code;
    public int subcode;
    public String lyric;

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getSubcode() {
        return subcode;
    }

    public void setSubcode(int subcode) {
        this.subcode = subcode;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }
}
