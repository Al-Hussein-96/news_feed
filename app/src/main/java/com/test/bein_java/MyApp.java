package com.test.bein_java;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        Picasso.Builder builder = new Picasso.Builder(this);
//        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
//        Picasso built = builder.build();
//        built.setIndicatorsEnabled(true);
//        built.setLoggingEnabled(true);
//        Picasso.setSingletonInstance(built);
    }
}
