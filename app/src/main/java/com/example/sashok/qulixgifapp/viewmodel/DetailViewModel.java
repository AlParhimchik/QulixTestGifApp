package com.example.sashok.qulixgifapp.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.sashok.qulixgifapp.data.GifDownloader;
import com.example.sashok.qulixgifapp.data.ImageNetworkCallBack;
import com.example.sashok.qulixgifapp.model.Gif;
import com.example.sashok.qulixgifapp.mvvm.viewmodel.ViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by sashok on 8.11.17.
 */

public class DetailViewModel extends ViewModel {
    public ObservableBoolean isProgress;
    public ObservableInt progress;
    public Gif mGif;
    private File fileGif;

    protected DetailViewModel(Context context, @Nullable State savedInstanceState) {
        super(context, savedInstanceState);
        isProgress = new ObservableBoolean(true);
        progress = new ObservableInt(0);
        if (savedInstanceState instanceof DetailViewModel.DetailViewModelState) {
            mGif =
                    ((DetailViewModelState) savedInstanceState).gif;
        }

    }

    @BindingAdapter("gifUrl")
    public static void setGifUrl(final GifImageView view, final DetailViewModel viewModel) {
        viewModel.startLoad();
        if (viewModel.mGif.gifFull.byteImage != null && viewModel.mGif.gifFull.byteImage.length != 0) {

            try {
                GifDrawable gifFromBytes = new GifDrawable(viewModel.mGif.gifFull.byteImage);
                view.setImageDrawable(gifFromBytes);
                gifFromBytes.start();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                viewModel.finishLoad();
            }
        } else
            new GifDownloader().download(viewModel.mGif.gifFull.imageUrl, new GifDownloader.ProgressListener() {
                        @Override
                        public void update(long bytesRead, long contentLength, boolean done) {
                            final int progress = (int) ((100 * bytesRead) / contentLength);
                            viewModel.progress.set(progress);

                        }
                    }, new ImageNetworkCallBack() {
                        @Override
                        public void onResponse(byte[] image) {
                            viewModel.mGif.gifFull.byteImage = image;
                            GifDrawable gifFromBytes = null;
                            viewModel.finishLoad();
                            try {
                                gifFromBytes = new GifDrawable(image);
                                view.setImageDrawable(gifFromBytes);
                                gifFromBytes.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            viewModel.finishLoad();
                        }
                    }
            );
    }

    @Override
    public State getInstanceState() {
        return new DetailViewModelState(this);
    }

    public void onShareFabClicked(View view) {
        shareGif();
    }

    public void shareGif() {
        try {
            String fileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
            fileGif = new File(mContext.getCacheDir(), fileName + ".gif");
            FileOutputStream outPutStream = new FileOutputStream(fileGif);

            FileOutputStream fos = new FileOutputStream(fileGif);
            fos.write(mGif.gifFull.byteImage);

            //
            outPutStream.flush();
            outPutStream.close();
            fileGif.setReadable(true, false);

            //share file
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileGif));
            shareIntent.setType("image/gif");
            if (mContext instanceof AppCompatActivity)
                ((AppCompatActivity) mContext).startActivityForResult(shareIntent, 1);
            else {
                Toast.makeText(mContext, "cann't share", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "error", Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        fileGif.delete();
    }

    public void startLoad() {
        isProgress.set(true);
    }

    public void finishLoad() {
        isProgress.set(false);
    }

    public void setItem(Gif item) {
        if (mGif == null) {
            mGif = item;
            notifyChange();
        }
    }

    public static class DetailViewModelState extends State {
        public static Creator<DetailViewModel.DetailViewModelState> CREATOR =
                new Creator<DetailViewModel.DetailViewModelState>() {
                    @Override
                    public DetailViewModel.DetailViewModelState createFromParcel(Parcel source) {
                        return new DetailViewModel.DetailViewModelState(source);
                    }

                    @Override
                    public DetailViewModel.DetailViewModelState[] newArray(int size) {
                        return new DetailViewModel.DetailViewModelState[size];
                    }
                };
        private final Gif gif;

        protected DetailViewModelState(DetailViewModel viewModel) {
            super(viewModel);
            gif = viewModel.mGif;
        }

        public DetailViewModelState(Parcel in) {
            super(in);
            gif = in.readParcelable(Gif.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(gif, flags);
        }
    }
}
