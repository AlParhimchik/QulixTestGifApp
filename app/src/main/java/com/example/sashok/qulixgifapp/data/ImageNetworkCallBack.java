package com.example.sashok.qulixgifapp.data;

/**
 * Created by sashok on 7.11.17.
 */

public interface ImageNetworkCallBack {
    public void onResponse(byte[] image);
    public  void onFailure(Exception e);
}
