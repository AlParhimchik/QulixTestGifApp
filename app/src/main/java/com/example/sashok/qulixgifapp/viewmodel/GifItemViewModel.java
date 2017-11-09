/*
 * Copyright (C) 2015 Brian Lee (@hiBrianLee)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.sashok.qulixgifapp.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.example.sashok.qulixgifapp.activity.DetailActivity;
import com.example.sashok.qulixgifapp.data.GifDownloader;
import com.example.sashok.qulixgifapp.data.ImageNetworkCallBack;
import com.example.sashok.qulixgifapp.model.Gif;
import com.example.sashok.qulixgifapp.mvvm.viewmodel.ItemViewModel;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;


public class GifItemViewModel extends ItemViewModel<Gif> {

    public Gif mGif;
    public ObservableInt isProgress;
    public ObservableInt showView;


    GifItemViewModel(@NonNull Context context) {
        super(context);
        isProgress = new ObservableInt(View.VISIBLE);
        showView = new ObservableInt(View.GONE);
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(final ImageView view, final GifItemViewModel viewModel) {
        viewModel.startLoad();
        if (viewModel.mGif != null)
            if (viewModel.mGif.gifPreview.byteImage != null && viewModel.mGif.gifPreview.byteImage.length != 0) {
                try {
                    GifDrawable gifFromBytes = new GifDrawable(viewModel.mGif.gifPreview.byteImage);
                    view.setImageDrawable(gifFromBytes);
                    gifFromBytes.start();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    viewModel.finishLoad();
                }
            } else
                new GifDownloader().download(viewModel.mGif.gifPreview.imageUrl, null, new ImageNetworkCallBack() {
                            @Override
                            public void onResponse(byte[] image) {
                                viewModel.mGif.gifPreview.byteImage = image;
                                GifDrawable gifFromBytes = null;
                                viewModel.finishLoad();
                                try {
                                    gifFromBytes = new GifDrawable(image);
                                    view.setImageDrawable(gifFromBytes);
                                    gifFromBytes.start();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Exception e) {
                                viewModel.finishLoad();
                            }
                        }
                );

    }

    @Override
    public void setItem(Gif item) {
        mGif = item;
        notifyChange();

    }

    public void onClick(View view) {
        startDetailActivity();
    }

    private void startDetailActivity() {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra("gif", mGif);
        mContext.startActivity(intent);
    }

    public void startLoad() {
        isProgress.set(View.VISIBLE);
        showView.set(View.GONE);
    }

    public void finishLoad() {
        isProgress.set(View.GONE);
        showView.set(View.VISIBLE);

    }

}
