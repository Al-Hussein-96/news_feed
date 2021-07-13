package com.test.bein_java.data.datesource;

import com.test.bein_java.data.News;

import java.util.List;

public interface NewsDataSource {

    void deleteNewsPermanently(News news);

    void moveToTrash(News news);

    void addNews(News news);

    interface NewsCallback {
        void onNewsLoaded(List<News> news);
    }

    void getNews(NewsCallback callback);

    void getDeletedNews(NewsCallback callback);


}
