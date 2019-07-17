package internet.com.larkmusic.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjning
 * created on: 2019-07-17 16:44
 * description:
 */
public class SearchLrcResponse implements Serializable {


    /**
     * ugccandidates : []
     * info : OK
     * status : 200
     * keyword :
     * ugc : 0
     * companys :
     * ugccount : 0
     * proposal : 39127546
     * has_complete_right : 0
     * candidates : [{"soundname":"","krctype":1,"id":"39127546","originame":"","accesskey":"76EB770581DF6F5FEC78634482500AAC","parinfo":[],"origiuid":"0",
     * "score":60,"hitlayer":1,"duration":250279,"adjust":0,"uid":"1000000010","song":"いつか天使になって あるいは青い鳥になって アダムとイブになって ありえないなら","transuid":"0",
     * "transname":"","sounduid":"0","nickname":"","contenttype":0,"singer":"三月のパンタシア","language":"日语"},{"soundname":"","krctype":2,"id":"39123741",
     * "originame":"","accesskey":"5D78B07BE118228A85144D761B862C1C","parinfo":[],"origiuid":"0","score":50,"hitlayer":6,"duration":250279,"adjust":0,
     * "uid":"1316895567","song":"いつか天使になって あるいは青い鳥になって アダムとイブになって ありえないなら","transuid":"0","transname":"","sounduid":"0","nickname":"Y","contenttype":0,
     * "singer":"三月のパンタシア","language":""}]
     */

    private String info;
    private int status;
    private String keyword;
    private int ugc;
    private String companys;
    private int ugccount;
    private String proposal;
    private int has_complete_right;
    private List<?> ugccandidates;
    private List<CandidatesBean> candidates;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getUgc() {
        return ugc;
    }

    public void setUgc(int ugc) {
        this.ugc = ugc;
    }

    public String getCompanys() {
        return companys;
    }

    public void setCompanys(String companys) {
        this.companys = companys;
    }

    public int getUgccount() {
        return ugccount;
    }

    public void setUgccount(int ugccount) {
        this.ugccount = ugccount;
    }

    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public int getHas_complete_right() {
        return has_complete_right;
    }

    public void setHas_complete_right(int has_complete_right) {
        this.has_complete_right = has_complete_right;
    }

    public List<?> getUgccandidates() {
        return ugccandidates;
    }

    public void setUgccandidates(List<?> ugccandidates) {
        this.ugccandidates = ugccandidates;
    }

    public List<CandidatesBean> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<CandidatesBean> candidates) {
        this.candidates = candidates;
    }

    public static class CandidatesBean implements Serializable {
        /**
         * soundname :
         * krctype : 1
         * id : 39127546
         * originame :
         * accesskey : 76EB770581DF6F5FEC78634482500AAC
         * parinfo : []
         * origiuid : 0
         * score : 60
         * hitlayer : 1
         * duration : 250279
         * adjust : 0
         * uid : 1000000010
         * song : いつか天使になって あるいは青い鳥になって アダムとイブになって ありえないなら
         * transuid : 0
         * transname :
         * sounduid : 0
         * nickname :
         * contenttype : 0
         * singer : 三月のパンタシア
         * language : 日语
         */

        private String soundname;
        private int krctype;
        private String id;
        private String originame;
        private String accesskey;
        private String origiuid;
        private int score;
        private int hitlayer;
        private int duration;
        private int adjust;
        private String uid;
        private String song;
        private String transuid;
        private String transname;
        private String sounduid;
        private String nickname;
        private int contenttype;
        private String singer;
        private String language;
        private List<?> parinfo;

        public String getSoundname() {
            return soundname;
        }

        public void setSoundname(String soundname) {
            this.soundname = soundname;
        }

        public int getKrctype() {
            return krctype;
        }

        public void setKrctype(int krctype) {
            this.krctype = krctype;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOriginame() {
            return originame;
        }

        public void setOriginame(String originame) {
            this.originame = originame;
        }

        public String getAccesskey() {
            return accesskey;
        }

        public void setAccesskey(String accesskey) {
            this.accesskey = accesskey;
        }

        public String getOrigiuid() {
            return origiuid;
        }

        public void setOrigiuid(String origiuid) {
            this.origiuid = origiuid;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getHitlayer() {
            return hitlayer;
        }

        public void setHitlayer(int hitlayer) {
            this.hitlayer = hitlayer;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getAdjust() {
            return adjust;
        }

        public void setAdjust(int adjust) {
            this.adjust = adjust;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getSong() {
            return song;
        }

        public void setSong(String song) {
            this.song = song;
        }

        public String getTransuid() {
            return transuid;
        }

        public void setTransuid(String transuid) {
            this.transuid = transuid;
        }

        public String getTransname() {
            return transname;
        }

        public void setTransname(String transname) {
            this.transname = transname;
        }

        public String getSounduid() {
            return sounduid;
        }

        public void setSounduid(String sounduid) {
            this.sounduid = sounduid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getContenttype() {
            return contenttype;
        }

        public void setContenttype(int contenttype) {
            this.contenttype = contenttype;
        }

        public String getSinger() {
            return singer;
        }

        public void setSinger(String singer) {
            this.singer = singer;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public List<?> getParinfo() {
            return parinfo;
        }

        public void setParinfo(List<?> parinfo) {
            this.parinfo = parinfo;
        }
    }
}
