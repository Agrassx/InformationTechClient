package com.muctr.informationtech.UI;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.muctr.informationtech.AppLogics.Article;
import com.muctr.informationtech.AppLogics.ArticleAdapter;
import com.muctr.informationtech.REST.ClientIntentService;
import com.muctr.informationtech.DataBase.DataBaseHandler;
import com.muctr.informationtech.R;
import com.muctr.informationtech.REST.TaskGetListHandler;

import java.util.ArrayList;
import java.util.List;


public class ArticleListFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, TaskGetListHandler {

    public static DataBaseHandler dataBase;
    private ClientIntentService clientIntentService;
    private final String ARTICLE_PREFS = "articlePrefs";
    private final String SAVE_SETTING_FAVORITE = "favorite";
    private static final int REQUEST_PATH = 1;
    private SharedPreferences settings;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isFavoriteList = false;
    private ArticleAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onPause() {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(SAVE_SETTING_FAVORITE, isFavoriteList);
        editor.apply();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("OnResume","Was");
        isFavoriteList = settings.getBoolean(SAVE_SETTING_FAVORITE, false);
//        MainActivity mainActivity = (MainActivity) getActivity();
//        getFavoriteArticles(mainActivity.getMenu().findItem(R.id.menu_favorite));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setItemsCanFocus(true);
        adapter = new ArticleAdapter(getActivity().getApplication(), new ArrayList<Article>());
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Context context = getActivity();
        clientIntentService = new ClientIntentService(context, this);
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        Intent intent = new Intent(context, ClientIntentService.class);
        clientIntentService.onHandleIntent(intent);
        dataBase = new DataBaseHandler(context);
        refreshAdapter(dataBase.getArticlesList());
        settings = context.getSharedPreferences(ARTICLE_PREFS, Context.MODE_PRIVATE);
        isFavoriteList = settings.getBoolean(SAVE_SETTING_FAVORITE, false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        FragmentManager fragmentManager = getFragmentManager();
        Fragment selectedItemFragment = new SelectedItemFragment();

        Article article = (Article) parent.getItemAtPosition(position);
        Bundle bundle = new Bundle();
        bundle.putString("name", article.getName());
        bundle.putString("url", article.getUrl());
        bundle.putBoolean("favorite", article.isFavorite());
        selectedItemFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .addToBackStack("FragmentList")
                .hide(ArticleListFragment.this)
                .replace(R.id.fragment_layout, selectedItemFragment)
                .commit();
    }

    public void getFavoriteArticles(MenuItem menuItem) {
        Log.e("onFavorite", "fragment");
        if (isFavoriteList) {
            isFavoriteList = false;
            refreshAdapter(dataBase.getArticlesList());
            menuItem.setIcon(R.drawable.ic_menu_star_24dp);
        } else {
            isFavoriteList = true;
            refreshAdapter(dataBase.getFavoriteArticlesList());
            menuItem.setIcon(R.drawable.ic_menu_star_choosed_24dp);
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        clientIntentService.refreshList(dataBase.getOffset());
    }

    @Override
    public void onTaskSuccessful(List<Article> list) {
        Log.e("onTaskSuccessful","complete :)");
        swipeRefreshLayout.setRefreshing(false);
        refreshAdapter(list);
        if (list.isEmpty()) {
            Toast.makeText(getActivity(), "Новых обновлнений нет", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Список обновлен", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTaskFailed() {
        Log.e("onTaskFailed","fail :(");
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getActivity(), "Ошибка!\nПроверьте подключение к интернету", Toast.LENGTH_LONG).show();
    }

    private void refreshAdapter(List<Article> list) {
        adapter.clear();
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }

}
