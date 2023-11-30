package com.ssf.homevisit.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollRecyclListener : RecyclerView.OnScrollListener() {
    override fun onScrolled(mRecyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(mRecyclerView, dx, dy)
        val layoutManager = mRecyclerView.layoutManager as LinearLayoutManager
        val visibleItemCount: Int = layoutManager.childCount
        val totalItemCount: Int = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                onLoadMore()
            }
        }
    }

    abstract fun onLoadMore()
    abstract fun isLoading(): Boolean
    abstract fun isLastPage(): Boolean
}