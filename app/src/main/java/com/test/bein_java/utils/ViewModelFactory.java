package com.test.bein_java.utils;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.test.bein_java.data.NewsRepository;
import com.test.bein_java.data.source.DummyDateSource;
import com.test.bein_java.ui.fragments.TrashViewModel;
import com.test.bein_java.view_mode.NewsFeedViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;

    private final NewsRepository mNewsRepository;



    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(
                            NewsRepository.getInstance(DummyDateSource.getInstance()));
                }
            }
        }
        return INSTANCE;
    }

    public NewsRepository getNewsRepository() {
        return mNewsRepository;
    }


    private ViewModelFactory(NewsRepository repository) {
        mNewsRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewsFeedViewModel.class)) {
            //noinspection unchecked
            return (T) new NewsFeedViewModel(mNewsRepository);
        }else if (modelClass.isAssignableFrom(TrashViewModel.class)) {
            //noinspection unchecked
            return (T) new TrashViewModel(mNewsRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}