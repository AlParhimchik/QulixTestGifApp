package com.example.sashok.qulixgifapp.data;

import android.support.annotation.Nullable;

import com.example.sashok.qulixgifapp.model.network.SearchResponse;
import com.example.sashok.qulixgifapp.model.network.TrendingResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.example.sashok.qulixgifapp.data.Constanse.SEARCH;
import static com.example.sashok.qulixgifapp.data.Constanse.TRENDING;

/**
 * Created by sashok on 4.11.17.
 */

public interface GihpyApi {
    @GET(SEARCH)
    Call<SearchResponse> search(@Query("api_key") String api_key, @Query("q") String string, @Query("limit") int limit, @Query("offset") int offset, @Query("lang") String lang);

    @GET(TRENDING)
    Call<TrendingResponse> trending(@Query("api_key") String api_key, @Query("limit") int limit,  @Query("offset") int offset);
}
