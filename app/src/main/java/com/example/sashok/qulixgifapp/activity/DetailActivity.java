package com.example.sashok.qulixgifapp.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.sashok.qulixgifapp.R;
import com.example.sashok.qulixgifapp.databinding.ActivityDetailBinding;
import com.example.sashok.qulixgifapp.model.Gif;
import com.example.sashok.qulixgifapp.mvvm.viewmodel.ViewModel;
import com.example.sashok.qulixgifapp.viewmodel.DetailViewModel;

/**
 * Created by sashok on 8.11.17.
 */

public class DetailActivity extends BaseActivity {

    private DetailViewModel mDetailViewModel;
    private ActivityDetailBinding mActivityDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mActivityDetailBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mActivityDetailBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (savedInstanceState == null) loadGif();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mDetailViewModel.onActivityResult(requestCode,resultCode,data);
    }

    private void loadGif() {
        if (getIntent().getExtras() != null) {
            Gif gif = getIntent().getExtras().getParcelable("gif");
            if (gif != null) {
                mDetailViewModel.setItem(gif);
            }
        }
    }


    @Nullable
    @Override
    protected ViewModel createViewModel(@Nullable ViewModel.State savedViewModelState) {
        mDetailViewModel = viewModelFactory.createDetailViewModel(this,
                savedViewModelState);
        mActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        mActivityDetailBinding.setViewModel(mDetailViewModel);
        return mDetailViewModel;
    }
}
