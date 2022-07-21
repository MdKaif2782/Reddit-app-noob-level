package com.example.reddit.objexts;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Post {
    private String title;
    private String author;
    private String subreddit;
    private String url;
    private int score;
    private int num_comments;
    private String post_hint;
    private String imageUrl;
    private int width;
    private int height;
    private String thumbnail;
    private Drawable thumbnailDrawable;
    private int screenWidth;



    public Post(String title, String author, String subreddit, String url, int score, int num_comments) {
        this.title = title;
        this.author = author;
        this.subreddit = subreddit;
        this.url = url;
        this.score = score;
        this.num_comments = num_comments;
    }
    public Post(String title, String author, String subreddit, String imageUrl, int score, int num_comments, int width, int height,String thumbnail,int screenWidth) {
        this.title = title;
        this.author = author;
        this.subreddit = subreddit;
        this.imageUrl = imageUrl;
        this.width=width;
        this.height=height;
        this.score = score;
        this.num_comments = num_comments;
        this.thumbnail=thumbnail;
        this.screenWidth=screenWidth;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public int getNum_comments() {
        return num_comments;
    }

    public String getUrl() {
        return url;
    }

    public int getScore() {
        return score;
    }

    public String getPost_hint() {
        return post_hint;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Drawable getThumbnailDrawable() {
        return thumbnailDrawable;
    }

    public int getScreenWidth() {
        return screenWidth;
    }




}
