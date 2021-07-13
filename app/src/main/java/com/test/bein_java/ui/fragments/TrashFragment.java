package com.test.bein_java.ui.fragments;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.bein_java.R;
import com.test.bein_java.databinding.TrashFragmentBinding;
import com.test.bein_java.utils.ViewModelFactory;
import com.test.bein_java.view_mode.NewsFeedViewModel;

public class TrashFragment extends Fragment {

    private TrashFragmentBinding binding;

    private TrashViewModel mViewModel;

    public static TrashFragment newInstance() {
        return new TrashFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = TrashFragmentBinding.inflate(inflater, container, false);

        mViewModel = obtainViewModel(requireActivity());

        return binding.getRoot();
    }
    public TrashViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return new ViewModelProvider(activity, factory).get(TrashViewModel.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}