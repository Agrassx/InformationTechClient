package com.muctr.informationtech.REST;

import com.muctr.informationtech.AppLogics.Article;

import java.util.List;

public interface TaskGetListHandler {
    void onTaskSuccessful(List<Article> list);
    void onTaskFailed();
}
