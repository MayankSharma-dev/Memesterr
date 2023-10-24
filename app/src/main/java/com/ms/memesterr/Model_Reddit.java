package com.ms.memesterr;

public class Model_Reddit {

    public static final int NULL_TYPE=0;
    public static final int IMAGE_TYPE=1;
    public static final int VIDEO_TYPE=2;


    private String subreddit;
    private String title;
    private String url;
    private String author;
    private String post_link;
    private String ups;
    private String image_previews;


    public String getImage_previews() {
        return image_previews;
    }

    private int type;


    Model_Reddit(String ups, String subreddit, String title, String url, String author, String post_link, int type, String image_previews){
        this.ups = ups;
        this.subreddit = subreddit;
        this.title = title;
        this.url = url;
        this.author = author;
        this.post_link = post_link;
        this.type = type;
        this.image_previews = image_previews;
    }
    Model_Reddit (int type){
        this.type = type;
    }


    public String getSubreddit() {
        return subreddit;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }

    public String getPost_link() {
        return post_link;
    }

    public String getUps() {
        return ups;
    }

    public int getType() {
        return type;
    }



}
