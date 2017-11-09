package com.example.sashok.qulixgifapp.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.sashok.qulixgifapp.adapter.GifListAdapter;
import com.example.sashok.qulixgifapp.mvvm.viewmodel.ViewModel;

/**
 * Created by sashok on 8.11.17.
 */

public class MainViewModel extends ViewModel {
    private GifListAdapter mAdapter;

    protected MainViewModel(@NonNull Context context, @Nullable State savedInstanceState) {
        super(context, savedInstanceState);
    }

}
