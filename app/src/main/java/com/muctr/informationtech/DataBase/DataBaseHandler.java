package com.muctr.informationtech.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.muctr.informationtech.Article;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {

    public static final String FAVORITE = "true";
    public static final String NOT_FAVORITE = "false";

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ArticleList";
    // Contacts table name
    private static final String TABLE_ARTICLE = "Articles";
    // Articles Table Columns names
    private static final String _ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_IS_FAVORITE = "favorite";


    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ARTICLES =
            "CREATE TABLE " + TABLE_ARTICLE + " (" + _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_URL + TEXT_TYPE + COMMA_SEP +
                    COLUMN_IS_FAVORITE + TEXT_TYPE + COMMA_SEP +
            " )";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ARTICLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addNewArticle(Article article) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, article.getName());
        values.put(COLUMN_URL, article.getUrl());
        values.put(COLUMN_IS_FAVORITE, NOT_FAVORITE);

        db.insert(TABLE_ARTICLE, null, values);
        db.close();
    }

    public List<Article> getArticlesList() {

        List<Article> articleList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ARTICLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Article article;

        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                if (cursor.getString(2).equals(FAVORITE)) {
                    article = new Article(cursor.getString(1), cursor.getString(2), true);
                } else {
                    article = new Article(cursor.getString(1), cursor.getString(2), false);
                }
                articleList.add(article);
            }
        }
        cursor.close();
        return articleList;
    }

    public int getArticlesCount() {
        String countQuery = "SELECT * FROM " + TABLE_ARTICLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }


    public int updateArticle(Article article) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, article.getName());
        values.put(COLUMN_URL, article.getUrl());
        values.put(COLUMN_IS_FAVORITE, isFavoriteString(article));

        return db.update(TABLE_ARTICLE, values, COLUMN_NAME + " = ?",
                new String[]{String.valueOf(article.getName())});
    }

    public void deleteArticle(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ARTICLE, COLUMN_NAME + " = ?",
                new String[] { String.valueOf(article.getName()) });
        db.close();
    }

    private String isFavoriteString(Article article) {
        return article.isFavorite() ? FAVORITE : NOT_FAVORITE;
    }
}
