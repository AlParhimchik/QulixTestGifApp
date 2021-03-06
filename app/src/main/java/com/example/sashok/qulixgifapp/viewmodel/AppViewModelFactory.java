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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.sashok.qulixgifapp.adapter.GifListAdapter;
import com.example.sashok.qulixgifapp.mvvm.viewmodel.ViewModel;


//singleton
public class AppViewModelFactory implements ViewModelFactory {

    private static ViewModelFactory mViewModelFactory;

    private AppViewModelFactory() {

    }

    public static ViewModelFactory getInstanсe() {
        if (mViewModelFactory == null) {
            synchronized (AppViewModelFactory.class) {
                if (mViewModelFactory == null) {
                    mViewModelFactory = new AppViewModelFactory();
                }
            }
        }
        return mViewModelFactory;
    }

    @NonNull
    @Override
    public DetailViewModel createDetailViewModel(@NonNull Context context, @Nullable ViewModel.State savedViewModelState) {
        return new DetailViewModel(context, savedViewModelState);
    }

    @NonNull
    @Override
    public GifListViewModel createGifListViewModel(@NonNull GifListAdapter adapter, @NonNull Context context, @Nullable ViewModel.State savedViewModelState) {
        return new GifListViewModel(adapter, context, savedViewModelState);
    }

    @NonNull
    @Override
    public GifItemViewModel createGifItemViewModel(@NonNull Context context) {
        return new GifItemViewModel(context);
    }

    @NonNull
    @Override
    public MainViewModel createMainViewModel(@NonNull Context context, @Nullable ViewModel.State savedViewModelState) {
        return new MainViewModel(context, savedViewModelState);
    }


}
