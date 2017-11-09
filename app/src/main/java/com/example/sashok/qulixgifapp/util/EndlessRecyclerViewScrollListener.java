package com.example.sashok.qulixgifapp.util;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * A listener to be added to {@link RecyclerView#addOnScrollListener(RecyclerView.OnScrollListener)}
 * to handle pagination on scrolling.
 * Code taken from https://gist.github.com/nesquena/d09dc68ff07e845cc622
 */
public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    // Sets the starting page index
    private int startingPageIndex = 0;
    // The current offset index of data you have loaded
    private int currentPage = startingPageIndex;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean isLoading = true;


    RecyclerView.LayoutManager layoutManager;


    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager,
            int startingPageIndex) {
        this.layoutManager = layoutManager;
        this.startingPageIndex = startingPageIndex;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = layoutManager.getItemCount();
        lastVisibleItemPosition =
                    ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.isLoading = true;
            }
        }

        if (isLoading && (totalItemCount > previousTotalItemCount)) {
            isLoading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (dy!=0 && !isLoading && (lastVisibleItemPosition + 1) >= totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount, view);
            isLoading = true;
        }
    }

    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

}