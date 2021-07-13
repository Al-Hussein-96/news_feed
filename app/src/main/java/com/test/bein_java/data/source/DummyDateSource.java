package com.test.bein_java.data.source;

import com.test.bein_java.data.News;
import com.test.bein_java.data.datesource.NewsDataSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DummyDateSource implements NewsDataSource {
    private static DummyDateSource INSTANCE;

    private final static List<News> NEWS_ARRAY_LIST = new ArrayList<>();

    static {
        NEWS_ARRAY_LIST.add(new News("1", "This sample is written in Java and based on the master branch which uses the following Architecture Components:", "https://assets.nst.com.my/images/articles/03xxworld_1596384698.jpg"));
        NEWS_ARRAY_LIST.add(new News("2", "This sample is writtenn", "https://www.joelapompe.net/wp-content/uploads/2019/01/bottle-natural-mont-dore-2017.jpg"));
        NEWS_ARRAY_LIST.add(new News("3", "the master branch which uses the following Architecture Components", "http://www.jkuat.ac.ke/departments/transport/wp-content/uploads/2018/11/natural-butterfly-image-for-mobile-1.jpg"));
        NEWS_ARRAY_LIST.add(new News("4", "This sample is written in Java and based on the master branch which uses the following Architecture Components", "https://assets.nst.com.my/images/articles/03xxworld_1596384698.jpg"));
        NEWS_ARRAY_LIST.add(new News("5", "This sample is written in Java", "https://www.joelapompe.net/wp-content/uploads/2019/01/bottle-natural-mont-dore-2017.jpg"));
        NEWS_ARRAY_LIST.add(new News("6", "This sample is written in Java and based on the master branch which uses the following Architecture Components", "https://akm-img-a-in.tosshub.com/indiatoday/images/story/202007/nature-3289812_1280_0.jpeg"));
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
        news.setDeleteAt(new Date());
    }

    @Override
    public void addNews(News news) {
        NEWS_ARRAY_LIST.add(news);
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
