package com.ms.memesterr;

public class Model_Twitter {

    private String post_user ;
    private String post_media_url ;
    private String post_text;
    private String post_url_profile_url;
    private String post_url;
    private int post_likes ;
    private int post_retweet;

    public Model_Twitter(String post_user, String post_media_url, String post_text, String post_url_profile_url, String post_url, int post_likes, int post_retweet) {
        this.post_user = post_user;
        this.post_media_url = post_media_url;
        this.post_text = post_text;
        this.post_url_profile_url = post_url_profile_url;
        this.post_url = post_url;
        this.post_likes = post_likes;
        this.post_retweet = post_retweet;
    }







    public String getPost_user() {
        return post_user;
    }

    public String getPost_media_url() {
        return post_media_url;
    }

    public String getPost_text() {
        return post_text;
    }

    public String getPost_url_profile_url() {
        return post_url_profile_url;
    }

    public String getPost_url() {
        return post_url;
    }

    public int getPost_likes() {
        return post_likes;
    }

    public int getPost_retweet() {
        return post_retweet;
    }


}
