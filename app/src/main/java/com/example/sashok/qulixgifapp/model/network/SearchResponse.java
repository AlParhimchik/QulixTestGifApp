package com.example.sashok.qulixgifapp.model.network;


import com.example.sashok.qulixgifapp.model.Gif;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sashok on 4.11.17.
 */

public class SearchResponse extends ListResponse<SearchResponse.SearchData> implements Serializable {
    public class SearchData implements Serializable {

        @SerializedName("images")
        public Gif gif;
    }


}
