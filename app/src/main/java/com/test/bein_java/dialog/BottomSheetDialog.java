package com.test.bein_java.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.test.bein_java.R;
import com.test.bein_java.data.News;

import org.jetbrains.annotations.NotNull;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private News news;

    public BottomSheetDialog(BottomSheetListener mListener, News news) {
        this.mListener = mListener;
        this.news = news;
    }


    @Override
    public void onCreate(@androidx.annotation.Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        Button button1 = v.findViewById(R.id.button1);
        Button button2 = v.findViewById(R.id.button2);
        button1.setOnClickListener(v1 -> {
            mListener.onButtonClicked(news);
            dismiss();
        });
        button2.setOnClickListener(v12 -> dismiss());



        return v;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@androidx.annotation.Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        Dialog dialog =  super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return dialog;

    }

    public interface BottomSheetListener {
        void onButtonClicked(News news);
    }


}
