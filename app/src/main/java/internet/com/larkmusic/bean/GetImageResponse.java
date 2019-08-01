package internet.com.larkmusic.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjning
 * created on: 2019-07-17 19:08
 * description:
 */
public class GetImageResponse implements Serializable {


    /**
     * status : 1
     * error_code : 0
     * data : [[{"album_name":"那个傻瓜","publish_date":"2018-12-08","category":"1","sizable_cover":"http://imge.kugou.com/stdmusic/{size}/20190101
     * /20190101091141878461.jpg","intro":"《那个傻瓜》由陈雪凝作词作曲，陈壹仟混音的一首独立流行歌。\n歌曲风格依旧沿袭了陈雪凝固有的风格。\n收纳于陈雪凝第二张专辑。","publish_company":"坚诚文化","album_id":"14291127",
     * "language_id":"1","is_publish":"1","quality":"2","heat":"336048","trans_param":{"special_tag":"0"},"special_tag":"0","grade":"7.6","type":"单曲专辑",
     * "cover":"20190101091141878461.jpg","language":"国语"}]]
     */

    private int status;
    private int error_code;
    private List<List<DataBean>> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<List<DataBean>> getData() {
        return data;
    }

    public void setData(List<List<DataBean>> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * album_name : 那个傻瓜
         * publish_date : 2018-12-08
         * category : 1
         * sizable_cover : http://imge.kugou.com/stdmusic/{size}/20190101/20190101091141878461.jpg
         * intro : 《那个傻瓜》由陈雪凝作词作曲，陈壹仟混音的一首独立流行歌。
         歌曲风格依旧沿袭了陈雪凝固有的风格。
         收纳于陈雪凝第二张专辑。
         * publish_company : 坚诚文化
         * album_id : 14291127
         * language_id : 1
         * is_publish : 1
         * quality : 2
         * heat : 336048
         * trans_param : {"special_tag":"0"}
         * special_tag : 0
         * grade : 7.6
         * type : 单曲专辑
         * cover : 20190101091141878461.jpg
         * language : 国语
         */

        private String album_name;
        private String publish_date;
        private String category;
        private String sizable_cover;
        private String intro;
        private String publish_company;
        private String album_id;
        private String language_id;
        private String is_publish;
        private String quality;
        private String heat;
        private TransParamBean trans_param;
        private String special_tag;
        private String grade;
        private String type;
        private String cover;
        private String language;

        public String getAlbum_name() {
            return album_name;
        }

        public void setAlbum_name(String album_name) {
            this.album_name = album_name;
        }

        public String getPublish_date() {
            return publish_date;
        }

        public void setPublish_date(String publish_date) {
            this.publish_date = publish_date;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSizable_cover() {
            return sizable_cover;
        }

        public void setSizable_cover(String sizable_cover) {
            this.sizable_cover = sizable_cover;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getPublish_company() {
            return publish_company;
        }

        public void setPublish_company(String publish_company) {
            this.publish_company = publish_company;
        }

        public String getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(String album_id) {
            this.album_id = album_id;
        }

        public String getLanguage_id() {
            return language_id;
        }

        public void setLanguage_id(String language_id) {
            this.language_id = language_id;
        }

        public String getIs_publish() {
            return is_publish;
        }

        public void setIs_publish(String is_publish) {
            this.is_publish = is_publish;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getHeat() {
            return heat;
        }

        public void setHeat(String heat) {
            this.heat = heat;
        }

        public TransParamBean getTrans_param() {
            return trans_param;
        }

        public void setTrans_param(TransParamBean trans_param) {
            this.trans_param = trans_param;
        }

        public String getSpecial_tag() {
            return special_tag;
        }

        public void setSpecial_tag(String special_tag) {
            this.special_tag = special_tag;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public static class TransParamBean implements Serializable{
            /**
             * special_tag : 0
             */

            private String special_tag;

            public String getSpecial_tag() {
                return special_tag;
            }

            public void setSpecial_tag(String special_tag) {
                this.special_tag = special_tag;
            }
        }
    }
}
