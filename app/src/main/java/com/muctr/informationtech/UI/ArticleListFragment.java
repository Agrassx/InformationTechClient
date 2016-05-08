package com.muctr.informationtech.UI;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArticleAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        dataBase = new DataBaseHandler(getActivity());
        List<Article> articleList = dataBase.getArticlesList();
        adapter.clear();
        adapter.addAll(articleList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        FragmentManager fragmentManager = getFragmentManager();
        Fragment selectedItemFragment = new SelectedItemFragment();

        Article article = (Article) parent.getItemAtPosition(position);
        Bundle bundle = new Bundle();
        Log.e("ArticleList", article.getName());
        bundle.putString("name", article.getName());
        bundle.putString("url", article.getUrl());
        selectedItemFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .addToBackStack("FragmentList")
                .hide(ArticleListFragment.this)
                .replace(R.id.fragment_layout, selectedItemFragment)
                .commit();
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
        adapter.clear();
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskFailed() {
        Log.e("onTaskFailed","fail :(");
        swipeRefreshLayout.setRefreshing(false);
    }
}
