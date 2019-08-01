package internet.com.larkmusic.bean.songDetailResponse;

import java.io.Serializable;

/**
 * Created by sjning
 * created on: 2019/5/15 下午5:55
 * description:
 */
public class SongDetailKuGou implements Serializable {
    private int status;
    private int err_code;
    private Data data;
    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }
    public int getErr_code() {
        return err_code;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }
}
