package com.example.myapplication.adapter;

import com.example.a130.NewPost;

import java.util.List;

public interface DataSender {

    public void onDataRecived(List<NewPost> listData);
}