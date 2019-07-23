package com.renyu.androidcommonlibrary.adapter.vlayout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.renyu.androidcommonlibrary.R

class GridAdapter(private val beans: List<String>, private val layoutHelper: LayoutHelper) : DelegateAdapter.Adapter<GridAdapter.GridViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_grid, parent, false)
        return GridViewHolder(view)
    }

    override fun getItemCount() = beans.size

    override fun onCreateLayoutHelper() = layoutHelper

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {

    }

    class GridViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView)
}