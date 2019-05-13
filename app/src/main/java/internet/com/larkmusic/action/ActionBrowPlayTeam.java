package internet.com.larkmusic.action;

import java.util.ArrayList;

import internet.com.larkmusic.bean.Album;


/**
 * Created by sjning
 * created on: 2019/1/12 下午3:02
 * description:
 */
public class ActionBrowPlayTeam {

    public static final int TYPE_BROW = 0;
    public static final int TYPE_TEAM_LIST = 1;


    public ArrayList<Album> teamList;

    public ActionBrowPlayTeam(ArrayList<Album> playTeamBean) {
        this.teamList = playTeamBean;
    }

}
