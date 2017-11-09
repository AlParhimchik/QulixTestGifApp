package com.example.sashok.qulixgifapp.model.network;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sashok on 5.11.17.
 */

public class ListResponse<T>  extends Response implements Serializable {
    @SerializedName("data")
    public List<T> data;
}
