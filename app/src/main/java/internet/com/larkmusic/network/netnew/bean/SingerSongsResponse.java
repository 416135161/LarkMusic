package internet.com.larkmusic.network.netnew.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjning
 * created on: 2019-07-29 15:02
 * description:
 */
public class SingerSongsResponse implements Serializable {


    /**
     * code : 0
     * data : {"list":[{"musicData":{"albumdesc":"","albumid":46775,"albummid":"0048MFSs1pk29x","albumname":"在你身边","alertid":21,"belongCD":9,"cdIdx":0,
     * "interval":254,"isonly":1,"label":"4611686018435776513","msgid":14,"rate":31,"singer":[{"id":174,"mid":"004Be55m1SJaLk","name":"张学友"}],"size128
     * ":4070965,"size320":10156346,"size5_1":0,"sizeape":29471380,"sizeflac":30832006,"sizeogg":5914914,"songid":559395,"songmid":"000FCzXs4eRBDw",
     * "songname":"烦恼歌","songorig":"烦恼歌","songtype":0,"strMediaMid":"000FCzXs4eRBDw","stream":13,"switch":17413891,"type":0,"vid":"d0012a0j8w0"}}]}
     * message : succ
     * subcode : 0
     */

    public int code;
    public DataBean data;
    public String message;
    public int subcode;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSubcode() {
        return subcode;
    }

    public void setSubcode(int subcode) {
        this.subcode = subcode;
    }

    public static class DataBean {
        public List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * musicData : {"albumdesc":"","albumid":46775,"albummid":"0048MFSs1pk29x","albumname":"在你身边","alertid":21,"belongCD":9,"cdIdx":0,"interval":254,
             * "isonly":1,"label":"4611686018435776513","msgid":14,"rate":31,"singer":[{"id":174,"mid":"004Be55m1SJaLk","name":"张学友"}],"size128":4070965,
             * "size320":10156346,"size5_1":0,"sizeape":29471380,"sizeflac":30832006,"sizeogg":5914914,"songid":559395,"songmid":"000FCzXs4eRBDw",
             * "songname":"烦恼歌","songorig":"烦恼歌","songtype":0,"strMediaMid":"000FCzXs4eRBDw","stream":13,"switch":17413891,"type":0,"vid":"d0012a0j8w0"}
             */

            public MusicDataBean musicData;

            public MusicDataBean getMusicData() {
                return musicData;
            }

            public void setMusicData(MusicDataBean musicData) {
                this.musicData = musicData;
            }

            public static class MusicDataBean {
                /**
                 * albumdesc : 
                 * albumid : 46775
                 * albummid : 0048MFSs1pk29x
                 * albumname : 在你身边
                 * alertid : 21
                 * belongCD : 9
                 * cdIdx : 0
                 * interval : 254
                 * isonly : 1
                 * label : 4611686018435776513
                 * msgid : 14
                 * rate : 31
                 * singer : [{"id":174,"mid":"004Be55m1SJaLk","name":"张学友"}]
                 * size128 : 4070965
                 * size320 : 10156346
                 * size5_1 : 0
                 * sizeape : 29471380
                 * sizeflac : 30832006
                 * sizeogg : 5914914
                 * songid : 559395
                 * songmid : 000FCzXs4eRBDw
                 * songname : 烦恼歌
                 * songorig : 烦恼歌
                 * songtype : 0
                 * strMediaMid : 000FCzXs4eRBDw
                 * stream : 13
                 * switch : 17413891
                 * type : 0
                 * vid : d0012a0j8w0
                 */

                public String albumdesc;
                public int albumid;
                public String albummid;
                public String albumname;
                public int alertid;
                public int belongCD;
                public int cdIdx;
                public int interval;
                public int isonly;
                public String label;
                public int msgid;
                public int rate;
                public int size128;
                public int size320;
                public int size5_1;
                public int sizeape;
                public int sizeflac;
                public int sizeogg;
                public int songid;
                public String songmid;
                public String songname;
                public String songorig;
                public int songtype;
                public String strMediaMid;
                public int stream;
                @SerializedName("switch")
                public int switchX;
                public int type;
                public String vid;
                public List<SingerBean> singer;

                public String getAlbumdesc() {
                    return albumdesc;
                }

                public void setAlbumdesc(String albumdesc) {
                    this.albumdesc = albumdesc;
                }

                public int getAlbumid() {
                    return albumid;
                }

                public void setAlbumid(int albumid) {
                    this.albumid = albumid;
                }

                public String getAlbummid() {
                    return albummid;
                }

                public void setAlbummid(String albummid) {
                    this.albummid = albummid;
                }

                public String getAlbumname() {
                    return albumname;
                }

                public void setAlbumname(String albumname) {
                    this.albumname = albumname;
                }

                public int getAlertid() {
                    return alertid;
                }

                public void setAlertid(int alertid) {
                    this.alertid = alertid;
                }

                public int getBelongCD() {
                    return belongCD;
                }

                public void setBelongCD(int belongCD) {
                    this.belongCD = belongCD;
                }

                public int getCdIdx() {
                    return cdIdx;
                }

                public void setCdIdx(int cdIdx) {
                    this.cdIdx = cdIdx;
                }

                public int getInterval() {
                    return interval;
                }

                public void setInterval(int interval) {
                    this.interval = interval;
                }

                public int getIsonly() {
                    return isonly;
                }

                public void setIsonly(int isonly) {
                    this.isonly = isonly;
                }

                public String getLabel() {
                    return label;
                }

                public void setLabel(String label) {
                    this.label = label;
                }

                public int getMsgid() {
                    return msgid;
                }

                public void setMsgid(int msgid) {
                    this.msgid = msgid;
                }

                public int getRate() {
                    return rate;
                }

                public void setRate(int rate) {
                    this.rate = rate;
                }

                public int getSize128() {
                    return size128;
                }

                public void setSize128(int size128) {
                    this.size128 = size128;
                }

                public int getSize320() {
                    return size320;
                }

                public void setSize320(int size320) {
                    this.size320 = size320;
                }

                public int getSize5_1() {
                    return size5_1;
                }

                public void setSize5_1(int size5_1) {
                    this.size5_1 = size5_1;
                }

                public int getSizeape() {
                    return sizeape;
                }

                public void setSizeape(int sizeape) {
                    this.sizeape = sizeape;
                }

                public int getSizeflac() {
                    return sizeflac;
                }

                public void setSizeflac(int sizeflac) {
                    this.sizeflac = sizeflac;
                }

                public int getSizeogg() {
                    return sizeogg;
                }

                public void setSizeogg(int sizeogg) {
                    this.sizeogg = sizeogg;
                }

                public int getSongid() {
                    return songid;
                }

                public void setSongid(int songid) {
                    this.songid = songid;
                }

                public String getSongmid() {
                    return songmid;
                }

                public void setSongmid(String songmid) {
                    this.songmid = songmid;
                }

                public String getSongname() {
                    return songname;
                }

                public void setSongname(String songname) {
                    this.songname = songname;
                }

                public String getSongorig() {
                    return songorig;
                }

                public void setSongorig(String songorig) {
                    this.songorig = songorig;
                }

                public int getSongtype() {
                    return songtype;
                }

                public void setSongtype(int songtype) {
                    this.songtype = songtype;
                }

                public String getStrMediaMid() {
                    return strMediaMid;
                }

                public void setStrMediaMid(String strMediaMid) {
                    this.strMediaMid = strMediaMid;
                }

                public int getStream() {
                    return stream;
                }

                public void setStream(int stream) {
                    this.stream = stream;
                }

                public int getSwitchX() {
                    return switchX;
                }

                public void setSwitchX(int switchX) {
                    this.switchX = switchX;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getVid() {
                    return vid;
                }

                public void setVid(String vid) {
                    this.vid = vid;
                }

                public List<SingerBean> getSinger() {
                    return singer;
                }

                public void setSinger(List<SingerBean> singer) {
                    this.singer = singer;
                }

                public static class SingerBean {
                    /**
                     * id : 174
                     * mid : 004Be55m1SJaLk
                     * name : 张学友
                     */

                    public int id;
                    public String mid;
                    public String name;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getMid() {
                        return mid;
                    }

                    public void setMid(String mid) {
                        this.mid = mid;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }
            }
        }
    }
}
