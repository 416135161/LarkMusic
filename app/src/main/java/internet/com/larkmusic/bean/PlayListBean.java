package internet.com.larkmusic.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Created by sjning
 * created on: 2019-07-19 11:37
 * description:
 */
public class PlayListBean extends LitePalSupport implements Serializable {
    private String name;
    private String icon;
    @Column(ignore = true)
    private int songAmount;

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public int getSongAmount() {
        return songAmount;
    }

    public void setSongAmount(int songAmount) {
        this.songAmount = songAmount;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
