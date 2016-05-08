package com.muctr.informationtech.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.muctr.informationtech.AppLogics.Article;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {

    public static final String FAVORITE = "true";
    public static final String NOT_FAVORITE = "false";

    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "ArticleList";
    // table name
    private static final String TABLE_ARTICLE = "Articles";
    // Articles Table Columns names
    private static final String _ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_IS_FAVORITE = "favorite";

    // table name
    private static final String TABLE_OFFSET = "Offset";
    //Offset Table columns names
    private static final String COLUMN_OFFSET = "offset";
    private static final String COLUMN_OFFSET_VERSION = "version";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ARTICLES =
            "CREATE TABLE " + TABLE_ARTICLE + " (" + _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_URL + TEXT_TYPE + COMMA_SEP +
                    COLUMN_IS_FAVORITE + TEXT_TYPE +
            " )";

    private static final String SQL_CREATE_OFFSET =
            "CREATE TABLE " + TABLE_OFFSET + " (" + _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_OFFSET + TEXT_TYPE + COMMA_SEP +
                    COLUMN_OFFSET_VERSION + INTEGER_TYPE +
                    " )";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ARTICLES);
        db.execSQL(SQL_CREATE_OFFSET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(DataBaseHandler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFSET);
        onCreate(db);
    }

    public void process(Article article) {
        if (article.isDelete()) {
            deleteArticle(article);
        } else if (isExist(article)) {
            updateArticle(article);
        } else {
            addArticle(article);
        }
    }

    public List<Article> getArticlesList() {

        List<Article> articleList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ARTICLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Article article;
        while (cursor.moveToNext()) {
            if (cursor.getString(3).equals(FAVORITE)) {
                article = new Article(cursor.getString(1), cursor.getString(2), true);
            } else {
                article = new Article(cursor.getString(1), cursor.getString(2), false);
            }
            articleList.add(article);
        }
        cursor.close();
        return articleList;
    }

    public List<Article> getFavoriteArticlesList() {

        List<Article> articleList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ARTICLE + " WHERE " + COLUMN_IS_FAVORITE + " = " + "'" + FAVORITE + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            if (cursor.getString(3).equals(FAVORITE)) {
                Article article = new Article(cursor.getString(1), cursor.getString(2), true);
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
        db.close();
        return cursor.getCount();
    }

    public void deleteAllArticles() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ARTICLE);
    }

    public int getOffset() {
        String selectQuery = "SELECT * FROM " + TABLE_OFFSET + " WHERE offset = 'offset'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int offset = 0;

        while (cursor.moveToNext()) {
            offset = cursor.getInt(2);
        }

        cursor.close();
        Log.e("offset in handler", String.valueOf(offset));

        return offset;
    }

    public void updateOffset(int offset) {
        if (getOffset() == 0) {
            addInitialOffset();
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OFFSET_VERSION, offset);
        db.update(
                TABLE_OFFSET,
                values,
                COLUMN_OFFSET + " = ?",
                new String[] { String.valueOf("offset") }
        );

    }

    private boolean isExist(Article article) {
        String countQuery = "SELECT name FROM " + TABLE_ARTICLE + " WHERE name = '" + article.getName() + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count != 0;
    }

    private void updateArticle(Article article) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, article.getName());
        values.put(COLUMN_URL, article.getUrl());
        values.put(COLUMN_IS_FAVORITE, isFavoriteString(article));
        db.update(TABLE_ARTICLE, values, COLUMN_NAME + " = ?",
                new String[]{ article.getName() });
    }

    private void addArticle(Article article) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, article.getName());
        values.put(COLUMN_URL, article.getUrl());
        values.put(COLUMN_IS_FAVORITE, NOT_FAVORITE);
        db.insert(TABLE_ARTICLE, null, values);
    }

    private void deleteArticle(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ARTICLE, COLUMN_NAME + " = ?",
                new String[] { String.valueOf(article.getName()) });
    }

    private String isFavoriteString(Article article) {
        return article.isFavorite() ? FAVORITE : NOT_FAVORITE;
    }

    private void addInitialOffset() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OFFSET, "offset");
        values.put(COLUMN_OFFSET_VERSION, 0);
        db.insert(TABLE_OFFSET, null, values);
    }
}
