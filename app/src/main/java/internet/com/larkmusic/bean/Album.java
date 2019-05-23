package internet.com.larkmusic.bean;

import java.io.Serializable;

/**
 * Created by sjning
 * created on: 2019/5/12 下午8:58
 * description:
 */
public class Album implements Serializable{
    private String imgUrl;
    private String name;
    private int id;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
