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
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.sashok.qulixgifapp.R;
import com.example.sashok.qulixgifapp.adapter.GifListAdapter;
import com.example.sashok.qulixgifapp.data.GiphyService;
import com.example.sashok.qulixgifapp.model.Gif;
import com.example.sashok.qulixgifapp.model.network.SearchResponse;
import com.example.sashok.qulixgifapp.model.network.TrendingResponse;
import com.example.sashok.qulixgifapp.mvvm.adapter.RecyclerViewAdapter;
import com.example.sashok.qulixgifapp.mvvm.viewmodel.RecyclerViewViewModel;
import com.example.sashok.qulixgifapp.mvvm.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sashok.qulixgifapp.data.Constanse.GIF_ON_PAGE;

public class GifListViewModel extends RecyclerViewViewModel {

    private static final String TAG = "TAG";
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    public ObservableInt gifProgress;
    public ObservableInt gifRecycler;
    public ObservableInt error;
    public ObservableField<String> errorMessage;
    private GifListAdapter adapter;
    private RequestTypeState trend_state;
    private RequestTypeState current_state;

    GifListViewModel(@NonNull final GifListAdapter adapter,
                     @NonNull Context context,
                     @Nullable State savedInstanceState) {
        super(context, savedInstanceState);
        gifProgress = new ObservableInt(View.VISIBLE);
        gifRecycler = new ObservableInt(View.GONE);
        error = new ObservableInt(View.GONE);
        errorMessage = new ObservableField<>("");
        if (savedInstanceState instanceof GifListState) {
            savedInstanceState.toModel(this);
            adapter.setItems(current_state.mGifs);
            if (gifProgress.get() == View.VISIBLE) loadData(current_state);
        } else {
            startDownload();
            trend_state = new RequestTypeState(0, RequestName.TRENDING, null);
            current_state = trend_state;
            loadData(current_state);
        }
        this.adapter = adapter;

    }

    public void startSearch(String query) {
        current_state = new RequestTypeState(0, RequestName.SEARCH, query);
        adapter.clearList();
        adapter.notifyDataSetChanged();
        startDownload();
        loadData(current_state);
    }

    public void searchClosed() {
        hideError();
        current_state = trend_state;
        adapter.clearList();
        adapter.setItems(current_state.mGifs);

    }

    public void loadData(RequestTypeState state) {
        if (state.mName == RequestName.TRENDING) {
            loadTrends(state);
        } else {
            search(state);
        }
    }

    public void loadTrends(final RequestTypeState state) {
        GiphyService.getInstanse().getTrending(GIF_ON_PAGE, GIF_ON_PAGE * state.current_page).enqueue(new Callback<TrendingResponse>() {
            @Override
            public void onResponse(Call<TrendingResponse> call, Response<TrendingResponse> response) {
                    List<Gif> loadedGifs = new ArrayList<Gif>();
                for (TrendingResponse.TrendingData gif_item : response.body().data
                        ) {
                    if (gif_item.gif.gifFull.imageUrl != "" && gif_item.gif.gifPreview.imageUrl != "") {
                        loadedGifs.add(gif_item.gif);
                    }
                }
                stopDownload();
                if (response.body().data.size() == GIF_ON_PAGE) {
                    state.current_page++;
                } else {
                    state.isLast = true;
                }
                adapter.removeProgressItem();
                current_state.mGifs.addAll(loadedGifs);
                adapter.setItems(loadedGifs);
                state.isLoading = false;
            }

            @Override
            public void onFailure(Call<TrendingResponse> call, Throwable t) {
                showError(t.getMessage());
            }
        });

    }

    public void search(final RequestTypeState state) {
        GiphyService.getInstanse().search(state.query, GIF_ON_PAGE, state.current_page).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                List<Gif> loadedGifs = new ArrayList<Gif>();
                for (SearchResponse.SearchData gif_item : response.body().data
                        ) {
                    if (gif_item.gif.gifFull.imageUrl != "" && gif_item.gif.gifPreview.imageUrl != "") {
                        loadedGifs.add(gif_item.gif);
                    }
                }
                stopDownload();
                if (response.body().data.size() == GIF_ON_PAGE) {
                    state.current_page++;
                } else {
                    state.isLast = true;
                }
                adapter.removeProgressItem();
                current_state.mGifs.addAll(loadedGifs);
                adapter.setItems(loadedGifs);
                state.isLoading = false;
                if (adapter.getItems().size() == 0) {
                    showError(mContext.getResources().getString(R.string.no_gifs));
                }

            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                showError(t.getMessage());

            }
        });
    }

    public void startDownload() {
        gifProgress.set(View.VISIBLE);
        gifRecycler.set(View.GONE);
        error.set(View.GONE);
    }

    public void stopDownload() {
        gifProgress.set(View.GONE);
        gifRecycler.set(View.VISIBLE);
        error.set(View.GONE);
    }

    public void showError(String errorStr) {
        gifRecycler.set(View.INVISIBLE);
        gifProgress.set(View.INVISIBLE);
        error.set(View.VISIBLE);
        errorMessage.set(errorStr);
    }

    public void hideError() {
        gifRecycler.set(View.VISIBLE);
        gifProgress.set(View.GONE);
        error.set(View.GONE);
    }

    @Override
    protected RecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case VIEW_ITEM:
                        return 1;
                    case VIEW_PROG:
                        return gridLayoutManager.getSpanCount();
                    default:
                        return -1;
                }
            }
        });
        return gridLayoutManager;
    }


    public void onLoadMore() {
        loadData(current_state);
    }

    @Override
    public void onRecyclerViewInitialized() {
        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy != 0) {
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    if (!current_state.isLoading && gridLayoutManager.findLastVisibleItemPosition() >= adapter.getItems().size() - 1 && !current_state.isLast && adapter.getItems().size() != 0) {
                        onLoadMore();
                        current_state.isLoading = true;
                        adapter.addProgressItem();
                    }
                }
            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public RecyclerViewViewModelState getInstanceState() {
        return new GifListState(this);
    }

    private enum RequestName {
        SEARCH, TRENDING;
    }

    private static class RequestTypeState implements Parcelable {
        public static final Creator<RequestTypeState> CREATOR = new Creator<RequestTypeState>() {
            @Override
            public RequestTypeState createFromParcel(Parcel in) {
                return new RequestTypeState(in);
            }

            @Override
            public RequestTypeState[] newArray(int size) {
                return new RequestTypeState[size];
            }
        };
        private final List<Gif> mGifs;
        private boolean isLoading;
        private boolean isLast;
        private int current_page;
        private RequestName mName;
        private String query;

        protected RequestTypeState(Parcel in) {
            isLoading = in.readByte() != 0;
            isLast = in.readByte() != 0;
            mGifs = in.createTypedArrayList(Gif.CREATOR);
            current_page = in.readInt();
            mName = (RequestName) in.readSerializable();
            query = in.readString();

        }

        public RequestTypeState(int page, RequestName name, @Nullable String query) {
            this.current_page = page;
            this.query = query;
            this.mName = name;
            this.mGifs = new ArrayList<>();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) (isLoading ? 1 : 0));
            dest.writeByte((byte) (isLast ? 1 : 0));
            dest.writeInt(current_page);
            dest.writeSerializable(mName);
            dest.writeString(query);
            dest.writeTypedList(mGifs);
        }
    }

    private static class GifListState extends RecyclerViewViewModelState {

        public static Parcelable.Creator<GifListState> CREATOR = new Parcelable.Creator<GifListState>() {
            @Override
            public GifListState createFromParcel(Parcel source) {
                return new GifListState(source);
            }

            @Override
            public GifListState[] newArray(int size) {
                return new GifListState[size];
            }
        };
        public int gifProgress;
        public int gifRecycler;
        public int error;
        public String errorMessage;
        private RequestTypeState trend_state;
        private RequestTypeState current_state;

        public GifListState(GifListViewModel viewModel) {
            super(viewModel);

            current_state = viewModel.current_state;
            trend_state = viewModel.trend_state;
            gifProgress = viewModel.gifProgress.get();
            gifRecycler = viewModel.gifRecycler.get();
            error = viewModel.error.get();
            errorMessage = viewModel.errorMessage.get();

        }

        public GifListState(Parcel in) {
            super(in);
            trend_state = in.readParcelable(RequestTypeState.class.getClassLoader());
            current_state = in.readParcelable(RequestTypeState.class.getClassLoader());
            gifProgress = in.readInt();
            gifRecycler = in.readInt();
            error = in.readInt();
            errorMessage = in.readString();
        }

        @Override
        public void toModel(ViewModel viewModel) {
            GifListState gifListState = null;
            GifListViewModel gifListViewModel = null;
            if (this instanceof GifListState) {
                gifListState = this;
                gifListViewModel = (GifListViewModel) viewModel;
                gifListViewModel.trend_state = gifListState.trend_state;
                gifListViewModel.current_state = gifListState.current_state;
                gifListViewModel.gifProgress.set(gifListState.gifProgress);
                gifListViewModel.gifRecycler.set(gifListState.gifRecycler);
                gifListViewModel.error.set(gifListState.error);
                gifListViewModel.errorMessage.set(gifListState.errorMessage);

            }

        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(current_state, flags);
            dest.writeParcelable(trend_state, flags);
            dest.writeInt(gifProgress);
            dest.writeInt(gifRecycler);
            dest.writeInt(error);
            dest.writeString(errorMessage);

        }
    }
}
