package internet.com.larkmusic.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import internet.com.larkmusic.bean.Song;

/**
 * Created by sjning
 * created on: 2019-06-18 18:47
 * description:
 */
public class HistoryService {
    public static final int MAX_SIZE = 30;

    private static HistoryService historyService;

    private HistoryService() {

    }

    public static HistoryService getInstance() {
        if (historyService == null) {
            historyService = new HistoryService();
        }
        return historyService;
    }

    public List<String> getList() {
        if (SpHelper.searchHistory == null) {
            Gson gs = new Gson();
            SpHelper.searchHistory = gs.fromJson(SpHelper.getDefault().getDefault().getString(SpHelper.KEY_SEARCH_HISTORY, "[]"),
                    new TypeToken<List<String>>() {
                    }.getType());
        }
        return SpHelper.searchHistory;
    }

    public void saveSong(String name) {
        if (SpHelper.searchHistory == null || name == null) {
            return;
        }

        for (int i = 0; i < SpHelper.searchHistory.size(); i++) {
            String item = SpHelper.searchHistory.get(i);
            //如果已保存，则返回
            if (TextUtils.equals(item, name)) {
                SpHelper.searchHistory.remove(i);
                break;
            }
        }
        SpHelper.searchHistory.add(0, name);
        if (SpHelper.searchHistory.size() > MAX_SIZE) {
            SpHelper.searchHistory = SpHelper.searchHistory.subList(0, MAX_SIZE);
        }
        SpHelper.getDefault().putString(SpHelper.KEY_SEARCH_HISTORY, new Gson().toJson(SpHelper.searchHistory));

    }

    public void removeSong(String name) {
        if (SpHelper.searchHistory == null || name == null) {
            return;
        }

        for (int i = 0; i < SpHelper.searchHistory.size(); i++) {
            String item = SpHelper.searchHistory.get(i);
            //如果已保存，则返回
            if (TextUtils.equals(item, name)) {
                SpHelper.searchHistory.remove(i);
                break;
            }
        }
        SpHelper.getDefault().putString(SpHelper.KEY_SEARCH_HISTORY, new Gson().toJson(SpHelper.searchHistory));

    }

}
