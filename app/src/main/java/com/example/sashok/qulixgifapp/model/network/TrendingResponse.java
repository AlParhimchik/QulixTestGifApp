package com.example.sashok.qulixgifapp.model.network;

import com.example.sashok.qulixgifapp.model.Gif;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sashok on 5.11.17.
 */

public class TrendingResponse extends ListResponse<TrendingResponse.TrendingData> implements Serializable {

    public class TrendingData implements Serializable {

        @SerializedName("images")
        public Gif gif;
    }

}