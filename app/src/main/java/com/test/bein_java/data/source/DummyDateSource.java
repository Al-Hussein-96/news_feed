package com.test.bein_java.data.source;

import com.test.bein_java.data.News;
import com.test.bein_java.data.datesource.NewsDataSource;

import java.util.ArrayList;
import java.util.List;

public class DummyDateSource implements NewsDataSource {
    private static DummyDateSource INSTANCE;

    private final static List<News> NEWS_ARRAY_LIST = new ArrayList<>();

    static {
        NEWS_ARRAY_LIST.add(new News("1", "Mohammad", "https://cdn.pixabay.com/user/2013/11/05/02-10-23-764_250x250.jpg"));
        NEWS_ARRAY_LIST.add(new News("2", "Mohammad", "https://raw.githubusercontent.com/facebook/fresco/master/docs/static/logo.png"));
        NEWS_ARRAY_LIST.add(new News("3", "Mohammad", "https://cdn.pixabay.com/user/2013/11/05/02-10-23-764_250x250.jpg"));
        NEWS_ARRAY_LIST.add(new News("4", "Mohammad", "https://raw.githubusercontent.com/facebook/fresco/master/docs/static/logo.png"));
        NEWS_ARRAY_LIST.add(new News("5", "Mohammad", "https://raw.githubusercontent.com/martinapinky/staggered-grid/master/app/src/main/res/drawable/kimsoohyun.jpg"));
        NEWS_ARRAY_LIST.add(new News("6", "Mohammad", "https://raw.githubusercontent.com/martinapinky/staggered-grid/master/app/src/main/res/drawable/parkseojoon.jpg"));
    }

    public static DummyDateSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DummyDateSource();
        }
        return INSTANCE;
    }


    @Override
    public void deleteNewsPermanently(News news) {
        NEWS_ARRAY_LIST.remove(news);

    }

    @Override
    public void moveToTrash(News news) {
        news.setDelete(true);
    }

    @Override
    public void getNews(NewsCallback callback) {
        List<News> activeNews = new ArrayList<>();

        for (News news : NEWS_ARRAY_LIST) {
            if (!news.isDelete())
                activeNews.add(news);
        }

        callback.onNewsLoaded(activeNews);
    }

    @Override
    public void getDeletedNews(NewsCallback callback) {
        List<News> deletedNews = new ArrayList<>();

        for (News news : NEWS_ARRAY_LIST) {
            if (news.isDelete())
                deletedNews.add(news);
        }

        callback.onNewsLoaded(deletedNews);

    }
}
