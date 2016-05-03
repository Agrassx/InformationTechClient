package com.muctr.informationtech;

import java.util.List;

public interface TaskGetListHandler {
    void onTaskSuccessful(List<Article> list);
    void onTaskFailed();
}
