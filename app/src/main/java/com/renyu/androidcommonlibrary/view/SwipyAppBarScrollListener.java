package com.renyu.androidcommonlibrary.view;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.AppBarLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

/**
 * Created by Clevo on 2016/8/29.
 * 解决 appbarLayout 刷新冲突
 */
public class SwipyAppBarScrollListener extends RecyclerView.OnScrollListener implements AppBarLayout.OnOffsetChangedListener {
    private AppBarLayout appBarLayout;
    private RecyclerView recyclerView;
    private ViewGroup refreshLayout;
    private boolean isAppBarLayoutOpen = true;
    private boolean isAppBarLayoutClose;

    public SwipyAppBarScrollListener(AppBarLayout appBarLayout, ViewGroup refreshLayout, RecyclerView recyclerView) {
        this.appBarLayout = appBarLayout;
        this.refreshLayout = refreshLayout;
        this.recyclerView = recyclerView;
        disptachScrollRefresh();
    }


    private void disptachScrollRefresh() {
        if (this.appBarLayout != null && this.recyclerView != null && refreshLayout != null) {
            this.appBarLayout.addOnOffsetChangedListener(this);
            this.recyclerView.addOnScrollListener(this);
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        dispatchScroll();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        isAppBarLayoutOpen = isAppBarLayoutOpen(verticalOffset);
        isAppBarLayoutClose = isAppBarLayoutClose(appBarLayout, verticalOffset);
        dispatchScroll();
    }

    private void dispatchScroll() {
        if (this.recyclerView != null && this.appBarLayout != null && this.refreshLayout != null) {
            //不可滚动
            if (!(recyclerView.canScrollVertically(-1) || recyclerView.canScrollVertically(1))) {
                refreshLayout.setEnabled(isAppBarLayoutOpen);
            }
            //可以滚动
            else {
                if (isAppBarLayoutOpen || isAppBarLayoutClose) {
                    //如果appbarLayout在打开的情况下并且不能下拉了
                    if (!recyclerView.canScrollVertically(-1) && isAppBarLayoutOpen) {
                        if (((SwipyRefreshLayout) refreshLayout).getDirection() != SwipyRefreshLayoutDirection.TOP) {
                            ((SwipyRefreshLayout) refreshLayout).setDirection(SwipyRefreshLayoutDirection.TOP);
                        }
                        refreshLayout.setEnabled(true);
                    }
                    //如果appbarLayout在关闭的情况下并且不能上拉了
                    else if (isAppBarLayoutClose && !recyclerView.canScrollVertically(1)) {
                        if (((SwipyRefreshLayout) refreshLayout).getDirection() != SwipyRefreshLayoutDirection.BOTTOM) {
                            ((SwipyRefreshLayout) refreshLayout).setDirection(SwipyRefreshLayoutDirection.BOTTOM);
                        }
                        refreshLayout.setEnabled(true);
                    } else {
                        refreshLayout.setEnabled(false);
                    }
                } else {
                    refreshLayout.setEnabled(false);
                }
            }
        }
    }

    /**
     * AppBarLayout 完全显示 打开状态
     *
     * @param verticalOffset
     * @return
     */
    public boolean isAppBarLayoutOpen(int verticalOffset) {
        return verticalOffset >= 0;
    }

    /**
     * AppBarLayout 关闭或折叠状态
     *
     * @param appBarLayout
     * @param verticalOffset
     * @return
     */
    public boolean isAppBarLayoutClose(AppBarLayout appBarLayout, int verticalOffset) {
        return appBarLayout.getTotalScrollRange() == Math.abs(verticalOffset);
    }

    /**
     * RecyclerView 滚动到底部 最后一条完全显示
     *
     * @param recyclerView
     * @return
     */
    public boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    /**
     * RecyclerView 滚动到顶端
     *
     * @param recyclerView
     * @return
     */
    public boolean isSlideToTop(RecyclerView recyclerView) {
        return recyclerView.computeVerticalScrollOffset() <= 0;
    }
}