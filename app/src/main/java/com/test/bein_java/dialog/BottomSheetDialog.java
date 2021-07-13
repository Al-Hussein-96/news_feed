package com.test.bein_java.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.test.bein_java.R;
import com.test.bein_java.data.News;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private News news;

    public BottomSheetDialog(BottomSheetListener mListener, News news) {
        this.mListener = mListener;
        this.news = news;
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

    public interface BottomSheetListener {
        void onButtonClicked(News news);
    }


}
