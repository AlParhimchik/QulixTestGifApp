package com.example.sashok.qulixgifapp.data;

/**
 * Created by sashok on 7.11.17.
 */

public interface ImageNetworkCallBack {
    void onResponse(byte[] image);

    void onFailure(Exception e);
}
