package com.muctr.informationtech;

public class Article {

    private String url;
    private String name;
    private boolean favorite;
    private int id;


    public Article(String name, String url) {
        this.name = name;
        this.url = url;
        this.favorite = false;
    }

    public Article(String name, String url, boolean favorite) {
        this.name = name;
        this.url = url;
        this.favorite = favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
