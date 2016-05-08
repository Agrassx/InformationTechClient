package com.muctr.informationtech.REST;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.muctr.informationtech.AppLogics.Article;
import com.muctr.informationtech.BuildConfig;
import com.muctr.informationtech.DataBase.DataBaseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

public class ClientIntentService extends IntentService implements Response.Listener<JSONObject>, Response.ErrorListener {

    private static final String URL_MAIN = BuildConfig.ServerAddress;
    private static final String URL_GET_LIST = "list?offset=";
    private TaskGetListHandler taskGetListHandler;
    private DataBaseHandler dataBaseHandler;
    private RequestQueue queue;
    private Context context;

    public ClientIntentService(Context context, TaskGetListHandler taskGetListHandler) {
        super("ClientIntentService");
        this.context = context;
        this.taskGetListHandler = taskGetListHandler;
    }

    @Override
    public void onHandleIntent(Intent intent) {

    }

    public void refreshList(int offset) {
        Log.e("Server address", BuildConfig.ServerAddress + URL_GET_LIST + String.valueOf(offset));
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, URL_MAIN + URL_GET_LIST + String.valueOf(offset), this, this);
        getQueue().add(jsObjRequest);
    }

    private RequestQueue getQueue() {
        if (queue == null) {
            return queue = Volley.newRequestQueue(context);
        } else {
            return queue;
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("onErrorResponse", error.toString());
        taskGetListHandler.onTaskFailed();
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e("onResponse", response.toString());
        dataBaseHandler = new DataBaseHandler(context);
        try {
            JSONArray articleArray = response.getJSONArray("articleList");
            int length = articleArray.length();
            for (int i = 0; i < length; i++) {
                Log.e("article", articleArray.getJSONObject(i).toString());
                Article article = new Article(
                        articleArray.getJSONObject(i).getString("name"),
                        articleArray.getJSONObject(i).getString("url")
                );
                dataBaseHandler.process(article);
            }
            Integer offset = response.getInt("offset");
            dataBaseHandler.updateOffset(offset);
            Log.e("offset", offset.toString());

        }
        catch (Exception exception) {
            Log.e("onResponse Exception", exception.getMessage());
        }
        taskGetListHandler.onTaskSuccessful(dataBaseHandler.getArticlesList());
    }
}
