package internet.com.larkmusic.network.netnew.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjning
 * created on: 2019-07-28 21:27
 * description:
 */
public class SearchSongResponse implements Serializable {


    /**
     * code : 0
     * data : {"song":{"list":[{"album":{"id":7165126,"mid":"0027xSBT3kSzyk","name":"一念之间","subtitle":"","title":"一念之间","title_hilight":"一念之间"},"mid
     * ":"000BbAjd0uSJYv","name":"一念之间","singer":[{"id":6499,"mid":"002azErJ0UcDN6","name":"张杰","title":"张杰","title_hilight":"<em>张杰<\/em>","type":0,"uin":0}
     * ],"title":"一念之间","title_hilight":"一念之间","file":{"media_mid":"004Rs6uh3a2ocN"}}],"totalnum":506}}
     */

    public int code;
    public DataBean data;

    public static class DataBean implements Serializable{


        public SongBean song;


        public static class SongBean implements Serializable{
            /**
             * list : [{"album":{"id":7165126,"mid":"0027xSBT3kSzyk","name":"一念之间","subtitle":"","title":"一念之间","title_hilight":"一念之间"},"mid":"000BbAjd0uSJYv
             * ","name":"一念之间","singer":[{"id":6499,"mid":"002azErJ0UcDN6","name":"张杰","title":"张杰","title_hilight":"<em>张杰<\/em>","type":0,"uin":0}],"title
             * ":"一念之间","title_hilight":"一念之间","file":{"media_mid":"004Rs6uh3a2ocN"}}]
             * totalnum : 506
             */

            public int totalnum;
            public List<ListBean> list;


            public static class ListBean implements Serializable{
                /**
                 * album : {"id":7165126,"mid":"0027xSBT3kSzyk","name":"一念之间","subtitle":"","title":"一念之间","title_hilight":"一念之间"}
                 * mid : 000BbAjd0uSJYv
                 * name : 一念之间
                 * singer : [{"id":6499,"mid":"002azErJ0UcDN6","name":"张杰","title":"张杰","title_hilight":"<em>张杰<\/em>","type":0,"uin":0}]
                 * title : 一念之间
                 * title_hilight : 一念之间
                 * file : {"media_mid":"004Rs6uh3a2ocN"}
                 */

                public AlbumBean album;
                public String mid;
                public String name;
                public String title;
                public String title_hilight;
                public FileBean file;
                public List<SingerBean> singer;

                public static class AlbumBean implements Serializable{
                    /**
                     * id : 7165126
                     * mid : 0027xSBT3kSzyk
                     * name : 一念之间
                     * subtitle :
                     * title : 一念之间
                     * title_hilight : 一念之间
                     */

                    public int id;
                    public String mid;
                    public String name;
                    public String subtitle;
                    public String title;
                    public String title_hilight;

                }

                public static class FileBean implements Serializable{
                    /**
                     * media_mid : 004Rs6uh3a2ocN
                     */

                    public String media_mid;


                }

                public static class SingerBean implements Serializable{
                    /**
                     * id : 6499
                     * mid : 002azErJ0UcDN6
                     * name : 张杰
                     * title : 张杰
                     * title_hilight : <em>张杰</em>
                     * type : 0
                     * uin : 0
                     */

                    public int id;
                    public String mid;
                    public String name;
                    public String title;
                    public int type;
                }
            }
        }
    }
}
