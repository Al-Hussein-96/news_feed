package com.test.bein_java.view_mode;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.test.bein_java.data.News;
import com.test.bein_java.data.NewsRepository;

import java.util.List;

public class NewsFeedViewModel extends ViewModel {
    private final NewsRepository mNewsRepository;
    private final MutableLiveData<List<News>> newsLiveData = new MutableLiveData<>();

    public NewsFeedViewModel(NewsRepository mNewsRepository) {
        this.mNewsRepository = mNewsRepository;
    }


    public void deleteNews(News news) {
        mNewsRepository.moveToTrash(news);

    }

    public void start() {
        loadNews();
    }

    public MutableLiveData<List<News>> getNewsLiveData() {
        return newsLiveData;
    }

    private void loadNews() {
        mNewsRepository.getNews(newsLiveData::setValue);
    }
}