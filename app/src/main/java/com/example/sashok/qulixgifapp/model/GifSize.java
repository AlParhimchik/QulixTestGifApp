package com.example.sashok.qulixgifapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sashok on 7.11.17.
 */

public class GifSize implements Parcelable {
    @SerializedName("url")
    public String imageUrl;
    @SerializedName("width")
    public String width;
    @SerializedName("height")
    public String height;
    public byte[] byteImage;

    protected GifSize(Parcel in) {
        imageUrl = in.readString();
        width = in.readString();
        height = in.readString();
        byteImage = in.createByteArray();
    }

    public static final Creator<GifSize> CREATOR = new Creator<GifSize>() {
        @Override
        public GifSize createFromParcel(Parcel in) {
            return new GifSize(in);
        }

        @Override
        public GifSize[] newArray(int size) {
            return new GifSize[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(width);
        dest.writeString(height);
        dest.writeByteArray(byteImage);
    }
}
