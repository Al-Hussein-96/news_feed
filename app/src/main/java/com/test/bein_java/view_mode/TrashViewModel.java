package com.test.bein_java.view_mode;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.test.bein_java.data.News;
import com.test.bein_java.data.NewsRepository;

import java.util.List;

public class TrashViewModel extends ViewModel {
    private final NewsRepository mNewsRepository;

    private final MutableLiveData<List<News>> newsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> noNewsLiveData = new MutableLiveData<>();


    public TrashViewModel(NewsRepository mNewsRepository) {
        this.mNewsRepository = mNewsRepository;
    }

    public MutableLiveData<List<News>> getNewsLiveData() {
        return newsLiveData;
    }

    public MutableLiveData<Boolean> getNoNewsLiveData() {
        return noNewsLiveData;
    }

    public void start() {
        loadDeletedNews();
    }

    private void loadDeletedNews() {
        mNewsRepository.getDeletedNews(news -> {
            if (news.isEmpty()) {
                noNewsLiveData.setValue(true);
            } else {
                noNewsLiveData.setValue(false);

                newsLiveData.setValue(news);
            }
        });

    }

    public void deleteNewsPermanently(News news) {
        mNewsRepository.deleteNewsPermanently(news);
    }
}