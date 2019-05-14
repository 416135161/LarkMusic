package internet.com.larkmusic.util;

import android.widget.TextView;

import internet.com.larkmusic.network.Config;

/**
 * Created by sjning
 * created on: 2019/1/12 下午9:23
 * description:
 */
public final class CommonUtil {


    public static void setTvBoldFace(TextView textView){
        textView.setTypeface(Config.tfLark);
        textView.getPaint().setFakeBoldText(true);
    }


}
