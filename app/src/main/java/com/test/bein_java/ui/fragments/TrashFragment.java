package com.test.bein_java.ui.fragments;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.bein_java.adapter.TrashAdapter;
import com.test.bein_java.data.News;
import com.test.bein_java.databinding.TrashFragmentBinding;
import com.test.bein_java.listener.SwipeToDeleteCallback;
import com.test.bein_java.utils.ViewModelFactory;
import com.test.bein_java.view_mode.TrashViewModel;

import java.util.ArrayList;

public class TrashFragment extends Fragment {

    private TrashFragmentBinding binding;
    private TrashViewModel mViewModel;
    private TrashAdapter trashAdapter;

    private ItemClicked itemClicked = news -> {
        mViewModel.deleteNewsPermanently(news);
    };

    public static TrashFragment newInstance() {
        return new TrashFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trashAdapter = new TrashAdapter(getContext(), new ArrayList<>(), itemClicked);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = TrashFragmentBinding.inflate(inflater, container, false);

        mViewModel = obtainViewModel(requireActivity());

        setUpRecyclerView();

        setObservers();

        return binding.getRoot();
    }

    private void setObservers() {
        mViewModel.getNewsLiveData().observe(getViewLifecycleOwner(), news -> {
            trashAdapter.replaceData(news);
        });

        mViewModel.getNoNewsLiveData().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                binding.recyclerView.setVisibility(View.GONE);
                binding.noFeed.getRoot().setVisibility(View.VISIBLE);
                binding.noFeed.addButton.setVisibility(View.GONE);
            } else {
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.noFeed.getRoot().setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start();
    }

    private void setUpRecyclerView() {
        binding.recyclerView.setAdapter(trashAdapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(trashAdapter));
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);
    }

    public TrashViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return new ViewModelProvider(activity, factory).get(TrashViewModel.class);
    }

    public interface ItemClicked {
        void onItemClicked(News news);
    }

}