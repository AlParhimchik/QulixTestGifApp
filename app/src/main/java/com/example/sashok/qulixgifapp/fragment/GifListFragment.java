package com.example.sashok.qulixgifapp.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sashok.qulixgifapp.R;
import com.example.sashok.qulixgifapp.activity.MainActivity;
import com.example.sashok.qulixgifapp.adapter.GifListAdapter;
import com.example.sashok.qulixgifapp.databinding.FragmentGifListBinding;
import com.example.sashok.qulixgifapp.mvvm.viewmodel.ViewModel;
import com.example.sashok.qulixgifapp.viewmodel.GifListViewModel;

/**
 * Created by sashok on 8.11.17.
 */

public class GifListFragment extends BaseFragment {
    private GifListViewModel mGifListViewModel;
    public static String TAG="GifListFragment";
    private GifListAdapter mGifListAdapter;
    @Nullable
    @Override
    protected ViewModel createAndBindViewModel(View root, Context context, @Nullable ViewModel.State savedViewModelState) {
        mGifListAdapter=new GifListAdapter(viewModelFactory,getActivity());
        mGifListViewModel=viewModelFactory.createGifListViewModel(mGifListAdapter,getActivity(),savedViewModelState);
        FragmentGifListBinding fragmentGifListBinding= DataBindingUtil.bind(getView());
        fragmentGifListBinding.setViewModel(mGifListViewModel);
        return  mGifListViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gif_list, container, false);
        return root;
    }

    public void startSeach(String query){
        mGifListViewModel.startSearch(query);
    }
    public void searchClosed(){
        mGifListViewModel.searchClosed();
    }
}
