package com.muctr.informationtech;

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

import org.json.JSONObject;

import java.util.ArrayList;

public class ClientIntentService extends IntentService implements Response.Listener<JSONObject>, Response.ErrorListener {

    private static final String URL_MAIN = BuildConfig.ServerAddress;
    private static final String URL_GET_LIST = "list?offset=0";
    private TaskGetListHandler taskGetListHandler;
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

    public void refreshList() {
        Log.e("Server address", BuildConfig.ServerAddress);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, URL_MAIN + URL_GET_LIST, this, this);
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
        taskGetListHandler.onTaskSuccessful(new ArrayList<Article>());
    }
}
