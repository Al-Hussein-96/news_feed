package com.test.bein_java.ui.fragments;

import androidx.lifecycle.ViewModel;

import com.test.bein_java.data.NewsRepository;

public class TrashViewModel extends ViewModel {
    private final NewsRepository mNewsRepository;

    public TrashViewModel(NewsRepository mNewsRepository) {
        this.mNewsRepository = mNewsRepository;
    }
}