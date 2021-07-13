package com.test.bein_java.utils;

import com.test.bein_java.data.News;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<News> getDummyData() {
        List<News> news = new ArrayList<>();

        news.add(new News("1","Mohammad","https://cdn.pixabay.com/user/2013/11/05/02-10-23-764_250x250.jpg"));
        news.add(new News("2","Mohammad","https://raw.githubusercontent.com/facebook/fresco/master/docs/static/logo.png"));
        news.add(new News("3","Mohammad","https://cdn.pixabay.com/user/2013/11/05/02-10-23-764_250x250.jpg"));
        news.add(new News("4","Mohammad","https://raw.githubusercontent.com/facebook/fresco/master/docs/static/logo.png"));
        news.add(new News("5","Mohammad","https://raw.githubusercontent.com/martinapinky/staggered-grid/master/app/src/main/res/drawable/kimsoohyun.jpg"));
        news.add(new News("6","Mohammad","https://raw.githubusercontent.com/martinapinky/staggered-grid/master/app/src/main/res/drawable/parkseojoon.jpg"));


        return news;
    }
}
