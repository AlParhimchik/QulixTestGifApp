package com.example.sashok.qulixgifapp.data;

import android.support.annotation.Nullable;


import com.example.sashok.qulixgifapp.model.network.SearchResponse;
import com.example.sashok.qulixgifapp.model.network.TrendingResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.sashok.qulixgifapp.data.Constanse.BASE_URL;


/**
 * Created by sashok on 4.11.17.
 */

public class GiphyService {
    private static Retrofit retrofit;
    private static volatile GiphyService giphyService;
    private static GihpyApi gihpyApi;

    public static GiphyService getInstanse() {
        if (giphyService == null) {
            synchronized (GiphyService.class) {
                if (giphyService == null)
                    giphyService = new GiphyService();
            }
        }
        return giphyService;
    }


    private GiphyService() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gihpyApi = retrofit.create(GihpyApi.class);
    }

    public Call<SearchResponse> search(String prhase, @Nullable int limit, @Nullable int offset) {
        return gihpyApi.search(Constanse.API_KEY, prhase, limit, offset,"ru");
    }

    public Call<TrendingResponse> getTrending(@Nullable int limit, @Nullable int offset) {
        return gihpyApi.trending(Constanse.API_KEY, limit, offset);
    }


}
