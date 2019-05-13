package internet.com.larkmusic.util;

import java.util.Collections;
import java.util.List;

/**
 * Created by sjning
 * created on: 2019/4/23 下午6:53
 * description:
 */
public final class SortUtil {
    private SortUtil() {
    }


    public static <T> List shuffle(List<T> list) {
        Collections.shuffle(list);
        return list;
    }
}