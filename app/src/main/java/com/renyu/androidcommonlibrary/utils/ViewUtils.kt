package com.renyu.androidcommonlibrary.utils

import android.databinding.BindingAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class ViewUtils {
    companion object {
        @JvmStatic
        @BindingAdapter(value = ["rvs"])
        fun <T : RecyclerView.ViewHolder> setRecyclerViews(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<T>) {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.adapter = adapter
        }
    }
}