package com.test.bein_java.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.test.bein_java.R;
import com.test.bein_java.data.News;
import com.test.bein_java.ui.fragments.NewsFeedFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News> news;
    private Context context;
    private NewsFeedFragment.ItemClicked itemClicked;

    public NewsAdapter(Context context, List<News> news, NewsFeedFragment.ItemClicked itemClicked) {
        this.context = context;
        this.news = news;
        this.itemClicked = itemClicked;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);







        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.mItem = news.get(position);

        holder.bind();

        holder.mView.setOnClickListener(v -> {
            itemClicked.onItemClicked(holder.mItem);
        });

        holder.share_icon.setOnClickListener(v -> {
            shareImage(holder.mItem.getImageUrl());
        });

    }

    private void shareImage(String imageUrl) {

        Glide.with(context).asBitmap().load(imageUrl).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);

                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), resource, "", null);
                Log.i("Mohammad", "path: " + resource);

                Uri screenshotUri = Uri.parse(path);

                shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                shareIntent.setType("image/jpeg");
                context.startActivity(Intent.createChooser(shareIntent, "share image"));
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }
        });

    }


    @Override
    public int getItemCount() {
        return news.size();
    }

    public List<News> getItems() {
        return news;
    }

    public void addNews(News newNews) {
        news.add(newNews);
        notifyItemInserted(news.size());
    }

    public void removeItemsMoreThan10() {
//        news.remove(10);
        news.subList(10,getItemCount()).clear();

        notifyItemRangeRemoved(10,getItemCount());
    }

    public void replaceData(List<News> news) {
        this.news.clear();
        this.news.addAll(news);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitle;
        public final TextView mContent;
        public final ImageView imageView;
        public final ImageView share_icon;
        public News mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.title);
            mContent = (TextView) view.findViewById(R.id.content);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            share_icon = (ImageView) view.findViewById(R.id.share_icon);

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
