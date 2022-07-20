package com.example.reddit.objexts;

public class Post {
    private String title;
    private String author;
    private String subreddit;
    private String url;
    private int score;
    private int num_comments;



    public Post(String title, String author, String subreddit, String url, int score, int num_comments) {
        this.title = title;
        this.author = author;
        this.subreddit = subreddit;
        this.url = url;
        this.score = score;
        this.num_comments = num_comments;
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

}
