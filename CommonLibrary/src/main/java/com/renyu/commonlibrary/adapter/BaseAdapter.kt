package com.renyu.commonlibrary.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.*

abstract class BaseAdapter<T: ViewDataBinding, D>(private val beans: ArrayList<D>) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    abstract fun initViews(): Int
    abstract fun dataVariableId(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<T>(LayoutInflater.from(parent.context), initViews(), parent, false)
        return BaseViewHolder(viewDataBinding)
    }

    override fun getItemCount(): Int = beans.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.viewDataBinding?.setVariable(dataVariableId(), beans[holder.layoutPosition])
        holder.viewDataBinding?.executePendingBindings()
    }

    class BaseViewHolder(viewDataBinding: ViewDataBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
        var viewDataBinding: ViewDataBinding? = viewDataBinding
    }
}