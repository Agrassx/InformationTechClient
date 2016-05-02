package com.muctr.informationtech;


import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

public class ItemFile implements Comparable<ItemFile> {

    private String name;
    private String data;
    private String date;
    private String path;
    private Drawable image;
    private boolean isDirectory = false;

    public ItemFile(String name, String data, String date, String path, Drawable image) {
        this.name = name;
        this.data = data;
        this.date = date;
        this.path = path;
        this.image = image;
    }

    public ItemFile(String name, String data, String date, String path, Drawable image, boolean isDirectory) {
        this.name = name;
        this.data = data;
        this.date = date;
        this.path = path;
        this.image = image;
        this.isDirectory = isDirectory;
    }

    public int compareTo(@NonNull ItemFile itemFile) {
        if(this.name != null) {
            return this.name.toLowerCase().compareTo(itemFile.getName().toLowerCase());
        } else {
            throw new IllegalArgumentException();
        }
    }

    public String getName()
    {
        return name;
    }

    public String getData()
    {
        return data;
    }

    public String getDate()
    {
        return date;
    }

    public String getPath()
    {
        return path;
    }

    public Drawable getImage() {
        return image;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

}
