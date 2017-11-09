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

package com.example.sashok.qulixgifapp.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.sashok.qulixgifapp.R;
import com.example.sashok.qulixgifapp.databinding.ActivityMainBinding;
import com.example.sashok.qulixgifapp.fragment.GifListFragment;
import com.example.sashok.qulixgifapp.mvvm.viewmodel.ViewModel;
import com.example.sashok.qulixgifapp.viewmodel.MainViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {
    public static String BUNDLE_QUERY = "query";
    public static String BUNDLE_ISFOCUSED = "focused";
    public static String BUNDLE_ISICONIFIED = "iconified";
    private String mQuery;
    private boolean isIconified = true;
    private boolean isFocused = false;
    private ActivityMainBinding mActivityMainBinding;
    private MainViewModel mMainViewModel;
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.toolbar_title);
        if (savedInstanceState == null) addListFragment();
        else {
            restoreState(savedInstanceState);
        }
    }

    private void restoreState(Bundle savedInstanceState) {
        mQuery = savedInstanceState.getString(BUNDLE_QUERY);
        isFocused = savedInstanceState.getBoolean(BUNDLE_ISFOCUSED);
        isIconified = savedInstanceState.getBoolean(BUNDLE_ISICONIFIED);
    }

    private void restoreSearchView() {
        mSearchView.setIconified(true);
        if (!isIconified) {
            mToolbar.getMenu().findItem(R.id.action_search).expandActionView();
            mSearchView.setQuery(mQuery, false);
            if (!isFocused) {
                mSearchView.clearFocus();
            }
        }
    }

    @Nullable
    @Override
    protected ViewModel createViewModel(@Nullable ViewModel.State savedViewModelState) {
        mMainViewModel = viewModelFactory.createMainViewModel(this,
                savedViewModelState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mActivityMainBinding.setViewModel(mMainViewModel);
        return mMainViewModel;
    }

    private void addListFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        GifListFragment fragment = new GifListFragment();
        transaction.replace(R.id.frame, fragment, GifListFragment.TAG);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.clearFocus();
                GifListFragment fragment = (GifListFragment) getSupportFragmentManager().findFragmentByTag(GifListFragment.TAG);
                if (fragment != null) {
                    fragment.startSeach(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                GifListFragment fragment = (GifListFragment) getSupportFragmentManager().findFragmentByTag(GifListFragment.TAG);
                if (fragment != null) {
                    fragment.searchClosed();
                }
                return true;
            }
        });
        restoreSearchView();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String query = mSearchView.getQuery().toString();
        boolean isFocused = mSearchView.hasFocus();
        boolean isIconified = mSearchView.isIconified();
        outState.putString(BUNDLE_QUERY, query);
        outState.putBoolean(BUNDLE_ISFOCUSED, isFocused);
        outState.putBoolean(BUNDLE_ISICONIFIED, isIconified);


    }

    @Override
    public void onBackPressed() {

        if (!mSearchView.isIconified()) {
            mSearchView.onActionViewCollapsed();
        } else {
            super.onBackPressed();
        }
    }

}
