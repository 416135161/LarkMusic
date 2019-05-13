package internet.com.larkmusic.action;

import java.util.ArrayList;

import internet.com.larkmusic.bean.Album;

/**
 * Created by sjning
 * created on: 2019/1/14 上午7:30
 * description:
 */
public class ActionListPlayTeam {
    public String from;
    public ArrayList<Album> playTeamBean;

    public ActionListPlayTeam(ArrayList<Album> playTeamBean, String from) {
        this.playTeamBean = playTeamBean;
        this.from = from;
    }

    public ArrayList<Album> getPlayTeamBean() {
        return playTeamBean;
    }

    public void setPlayTeamBean(ArrayList<Album> playTeamBean) {
        this.playTeamBean = playTeamBean;
    }
}
