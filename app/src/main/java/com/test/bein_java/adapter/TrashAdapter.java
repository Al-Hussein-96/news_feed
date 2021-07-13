package com.test.bein_java.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.test.bein_java.R;
import com.test.bein_java.data.News;
import com.test.bein_java.ui.fragments.TrashFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TrashAdapter extends RecyclerView.Adapter<TrashAdapter.ViewHolder> {
    private final Context context;
    private final List<News> deletedNews;
    private TrashFragment.ItemClicked itemDeleted;

    public TrashAdapter(Context context, List<News> deletedNews, TrashFragment.ItemClicked itemClicked) {
        this.context = context;
        this.deletedNews = deletedNews;
        this.itemDeleted = itemClicked;
    }

    public Context getContext() {
        return context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_deleted, parent, false);

        return new TrashAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.mItem = deletedNews.get(position);

        holder.bind();
    }

    @Override
    public int getItemCount() {
        return deletedNews.size();
    }

    public void deleteTask(int position) {
        itemDeleted.onItemClicked(deletedNews.get(position));
        deletedNews.remove(position);
    }

    public void replaceData(List<News> news) {
        deletedNews.clear();
        deletedNews.addAll(news);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitle;
        public final TextView mContent;
        public final TextView textViewDate;
        public final ImageView imageView;
        public News mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.title);
            textViewDate = (TextView) view.findViewById(R.id.textView_date);
            mContent = (TextView) view.findViewById(R.id.content);
            imageView = (ImageView) view.findViewById(R.id.imageView);

        }

        public void bind() {
            mTitle.setText(mItem.getTitle());
            mContent.setText(mItem.getContent());

            Glide.with(mView.getContext())
                    .load(mItem.getImageUrl())
                    .placeholder(R.drawable.flower)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);


//            Picasso.get()
//                    .load(mItem.getImageUrl())
//                    .into(imagePost);
        }
    }

}
