package com.muctr.informationtech.AppLogics;

public class Article {

    private String url;
    private String name;
    private boolean favorite;
    private int id;
    private boolean isDelete = false;


    public Article(String name, String url) {
        if (url.isEmpty()) {
            isDelete = true;
        }
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

    public boolean isDelete() {
        return isDelete;
    }
}
