package com.renyu.androidcommonlibrary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.databinding.AdapterSmartrefreshlayoutBinding

class SmartRefreshLayoutAdapter(private val beans: ArrayList<String>) :
    RecyclerView.Adapter<SmartRefreshLayoutAdapter.SmartRefreshLayoutViewHolder>() {

    class SmartRefreshLayoutViewHolder(val viewDataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SmartRefreshLayoutViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<AdapterSmartrefreshlayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.adapter_smartrefreshlayout,
            parent,
            false
        )
        return SmartRefreshLayoutViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: SmartRefreshLayoutViewHolder, position: Int) {
        holder.viewDataBinding.executePendingBindings()
    }

    override fun getItemCount() = beans.size
}