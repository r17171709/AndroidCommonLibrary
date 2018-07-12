package com.renyu.androidcommonlibrary.binding

import android.databinding.BindingAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by Administrator on 2018/7/12.
 */
object BindingAdapters {
    @JvmStatic
    @BindingAdapter(value = ["rvs"])
    fun <T : RecyclerView.ViewHolder> setRecyclerViews(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<T>) {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter
    }
}