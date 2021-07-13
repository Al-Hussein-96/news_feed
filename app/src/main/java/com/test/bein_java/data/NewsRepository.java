package com.test.bein_java.data;

import com.test.bein_java.data.datesource.NewsDataSource;
import com.test.bein_java.data.source.DummyDateSource;

public class NewsRepository implements NewsDataSource {
    private volatile static NewsRepository INSTANCE = null;

    private final DummyDateSource dummyDateSource;

    private NewsRepository(DummyDateSource dummyDateSource) {
        this.dummyDateSource = dummyDateSource;
    }

    public static NewsRepository getInstance(NewsDataSource newsDataSource) {
        if (INSTANCE == null) {
            synchronized (NewsRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NewsRepository((DummyDateSource) newsDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void deleteNewsPermanently(News news) {
        dummyDateSource.deleteNewsPermanently(news);
    }

    @Override
    public void moveToTrash(News news) {
        dummyDateSource.moveToTrash(news);
    }

    @Override
    public void getNews(NewsCallback callback) {
        dummyDateSource.getNews(callback);
    }

    @Override
    public void getDeletedNews(NewsCallback callback) {
        dummyDateSource.getDeletedNews(callback::onNewsLoaded);

    }
}
