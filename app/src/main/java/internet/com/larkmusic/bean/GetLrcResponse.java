package internet.com.larkmusic.bean;

import java.io.Serializable;

/**
 * Created by sjning
 * created on: 2019-07-17 16:54
 * description:
 */
public class GetLrcResponse implements Serializable {


    /**
     * charset : utf8
     * content : WzAwOjAx
     * fmt : lrc
     * info : OK
     * status : 200
     */

    private String charset;
    private String content;
    private String fmt;
    private String info;
    private int status;

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFmt() {
        return fmt;
    }

    public void setFmt(String fmt) {
        this.fmt = fmt;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
