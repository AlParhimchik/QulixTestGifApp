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

package com.example.sashok.qulixgifapp.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sashok.qulixgifapp.R;
import com.example.sashok.qulixgifapp.databinding.GifItemBinding;
import com.example.sashok.qulixgifapp.model.Gif;
import com.example.sashok.qulixgifapp.mvvm.adapter.RecyclerViewAdapter;
import com.example.sashok.qulixgifapp.viewmodel.GifItemViewModel;
import com.example.sashok.qulixgifapp.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;


public class GifListAdapter
        extends RecyclerViewAdapter<Gif, GifItemViewModel> {

    private final ViewModelFactory viewModelFactory;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int progressItemPosition = -1;

    public GifListAdapter(ViewModelFactory viewModelFactory,
                          @NonNull Context context) {
        super(context);
        this.viewModelFactory = viewModelFactory;
    }

    @Override
    public int getItemViewType(int position) {
        int view_type = items.get(position) == null ? VIEW_PROG : VIEW_ITEM;
        return view_type;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gif_item, parent, false);
            GifItemViewModel viewModel =
                    viewModelFactory.createGifItemViewModel(mContext);

            GifItemBinding binding = GifItemBinding.bind(itemView);
            binding.setViewModel(viewModel);

            return new GifViewHolder(itemView, binding, viewModel);

        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar, parent, false);
            ItemViewHolder itemViewHolder = new ProgressBarViewHolder(v);
            return itemViewHolder;
        }

    }

    public void setItems(List<Gif> newItems) {
        int inserted_gif_count = newItems.size();
        items.addAll(newItems);
        notifyItemRangeInserted(items.size() - inserted_gif_count, inserted_gif_count);
    }

    public ArrayList<Gif> getItems() {
        return items;
    }

    public void clearList() {
        items.clear();
        notifyDataSetChanged();
    }

    static class GifViewHolder
            extends ItemViewHolder<Gif, GifItemViewModel> {

        public GifViewHolder(View itemView, ViewDataBinding binding,
                             GifItemViewModel viewModel) {
            super(itemView, binding, viewModel);

        }
    }

    static class ProgressBarViewHolder extends ItemViewHolder {

        public ProgressBarViewHolder(View v) {
            super(v, null, null);
        }
    }

    public void addProgressItem() {
        items.add(null);
        progressItemPosition = items.size() - 1;
        notifyItemInserted(progressItemPosition);
    }

    public void removeProgressItem() {
        if (items.size()!=0 && progressItemPosition!=-1) {
            items.remove(progressItemPosition);
            notifyItemRemoved(progressItemPosition);

        }
    }
}
