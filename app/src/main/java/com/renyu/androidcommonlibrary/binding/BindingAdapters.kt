package com.renyu.androidcommonlibrary.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

    @JvmStatic
    @BindingAdapter(value = ["refresh"])
    fun autoRefreshAdapter(recyclerView: RecyclerView, boolean: Boolean) {
        if (boolean) {
            recyclerView.adapter!!.notifyDataSetChanged()
        }
    }
}