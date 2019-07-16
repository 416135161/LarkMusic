package internet.com.larkmusic.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Created by sjning
 * created on: 2019-07-16 12:39
 * description:
 */
public class SavedStateBean extends LitePalSupport implements Serializable {
    public static final String TAG_STATE = "11";

    @Column(unique = true, defaultValue = "unknown")
    private String tag;
    private int index;
    private String currentPlayList;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCurrentPlayList() {
        return currentPlayList;
    }

    public void setCurrentPlayList(String currentPlayList) {
        this.currentPlayList = currentPlayList;
    }
}
