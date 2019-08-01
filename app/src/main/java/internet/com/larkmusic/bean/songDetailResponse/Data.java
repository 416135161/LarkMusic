/**
  * Copyright 2019 bejson.com 
  */
package internet.com.larkmusic.bean.songDetailResponse;
import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2019-01-08 20:9:52
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Data implements Serializable {

    private String hash;
    private int timelength;
    private long filesize;
    private String audio_name;
    private int have_album;
    private String album_name;
    private int album_id;
    private String img;
    private int have_mv;
    private int video_id;
    private String author_name;
    private String song_name;
    private String lyrics;
    private int author_id;
    private int privilege;
    private String privilege2;
    private String play_url;
    private List<Authors> authors;
    private int bitrate;
    public void setHash(String hash) {
         this.hash = hash;
     }
     public String getHash() {
         return hash;
     }

    public void setTimelength(int timelength) {
         this.timelength = timelength;
     }
     public int getTimelength() {
         return timelength;
     }

    public void setFilesize(long filesize) {
         this.filesize = filesize;
     }
     public long getFilesize() {
         return filesize;
     }

    public void setAudio_name(String audio_name) {
         this.audio_name = audio_name;
     }
     public String getAudio_name() {
         return audio_name;
     }

    public void setHave_album(int have_album) {
         this.have_album = have_album;
     }
     public int getHave_album() {
         return have_album;
     }

    public void setAlbum_name(String album_name) {
         this.album_name = album_name;
     }
     public String getAlbum_name() {
         return album_name;
     }

    public void setAlbum_id(int album_id) {
         this.album_id = album_id;
     }
     public int getAlbum_id() {
         return album_id;
     }

    public void setImg(String img) {
         this.img = img;
     }
     public String getImg() {
         return img;
     }

    public void setHave_mv(int have_mv) {
         this.have_mv = have_mv;
     }
     public int getHave_mv() {
         return have_mv;
     }

    public void setVideo_id(int video_id) {
         this.video_id = video_id;
     }
     public int getVideo_id() {
         return video_id;
     }

    public void setAuthor_name(String author_name) {
         this.author_name = author_name;
     }
     public String getAuthor_name() {
         return author_name;
     }

    public void setSong_name(String song_name) {
         this.song_name = song_name;
     }
     public String getSong_name() {
         return song_name;
     }

    public void setLyrics(String lyrics) {
         this.lyrics = lyrics;
     }
     public String getLyrics() {
         return lyrics;
     }

    public void setAuthor_id(int author_id) {
         this.author_id = author_id;
     }
     public int getAuthor_id() {
         return author_id;
     }

    public void setPrivilege(int privilege) {
         this.privilege = privilege;
     }
     public int getPrivilege() {
         return privilege;
     }

    public void setPrivilege2(String privilege2) {
         this.privilege2 = privilege2;
     }
     public String getPrivilege2() {
         return privilege2;
     }

    public void setPlay_url(String play_url) {
         this.play_url = play_url;
     }
     public String getPlay_url() {
         return play_url;
     }

    public void setAuthors(List<Authors> authors) {
         this.authors = authors;
     }
     public List<Authors> getAuthors() {
         return authors;
     }

    public void setBitrate(int bitrate) {
         this.bitrate = bitrate;
     }
     public int getBitrate() {
         return bitrate;
     }

}