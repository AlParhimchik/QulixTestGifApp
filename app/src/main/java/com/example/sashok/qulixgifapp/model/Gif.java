package com.example.sashok.qulixgifapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sashok on 5.11.17.
 */

public class Gif implements Parcelable {
    @SerializedName("fixed_width_downsampled")
    public GifSize gifPreview;

    @SerializedName("downsized_large")
    public GifSize gifFull;

    @SerializedName("original_still")
    public GifSize still;

    protected Gif(Parcel in) {
        gifPreview = in.readParcelable(Gif.class.getClassLoader());
        gifFull = in.readParcelable(Gif.class.getClassLoader());
        still = in.readParcelable(Gif.class.getClassLoader());
    }

    public static final Creator<Gif> CREATOR = new Creator<Gif>() {
        @Override
        public Gif createFromParcel(Parcel in) {
            return new Gif(in);
        }

        @Override
        public Gif[] newArray(int size) {
            return new Gif[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(gifPreview,flags);
        dest.writeParcelable(gifFull,flags);
        dest.writeParcelable(still,flags);
    }
}
