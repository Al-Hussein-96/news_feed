package com.test.bein_java.data.datesource;

import com.test.bein_java.data.News;

import java.util.List;

public interface NewsDataSource {
    interface NewsCallback {
        void onNewsLoaded(List<News> news);
    }

    void getNews(NewsCallback callback);

}
