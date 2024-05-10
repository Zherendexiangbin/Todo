package net.onest.time.entity.dayword;

import java.util.List;

public class QuoteData {
    private String id;
    private String content;
    private String author;
    private String assign_date;
    private Object ad_url;
    private List<String> origin_img_urls;
    private TrackObject track_object;
    private int join_num;
    private List<String> share_img_urls;
    private Object daily_audio_urls;
    private List<String> poster_img_urls;
    private String translation;
    private ShareUrls share_urls;
    private String share_url;
    private String note;

    // getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAssign_date() {
        return assign_date;
    }

    public void setAssign_date(String assign_date) {
        this.assign_date = assign_date;
    }

    public Object getAd_url() {
        return ad_url;
    }

    public void setAd_url(Object ad_url) {
        this.ad_url = ad_url;
    }

    public List<String> getOrigin_img_urls() {
        return origin_img_urls;
    }

    public void setOrigin_img_urls(List<String> origin_img_urls) {
        this.origin_img_urls = origin_img_urls;
    }

    public TrackObject getTrack_object() {
        return track_object;
    }

    public void setTrack_object(TrackObject track_object) {
        this.track_object = track_object;
    }

    public int getJoin_num() {
        return join_num;
    }

    public void setJoin_num(int join_num) {
        this.join_num = join_num;
    }

    public List<String> getShare_img_urls() {
        return share_img_urls;
    }

    public void setShare_img_urls(List<String> share_img_urls) {
        this.share_img_urls = share_img_urls;
    }

    public Object getDaily_audio_urls() {
        return daily_audio_urls;
    }

    public void setDaily_audio_urls(Object daily_audio_urls) {
        this.daily_audio_urls = daily_audio_urls;
    }

    public List<String> getPoster_img_urls() {
        return poster_img_urls;
    }

    public void setPoster_img_urls(List<String> poster_img_urls) {
        this.poster_img_urls = poster_img_urls;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public ShareUrls getShare_urls() {
        return share_urls;
    }

    public void setShare_urls(ShareUrls share_urls) {
        this.share_urls = share_urls;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}