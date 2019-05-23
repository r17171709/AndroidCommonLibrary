package com.renyu.androidcommonlibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.renyu.androidcommonlibrary.R

/**
 * Created by Administrator on 2019/5/23.
 */
class FlexAdapter(private val beans: ArrayList<String>) : RecyclerView.Adapter<FlexAdapter.FlexViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlexViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_flex, parent, false)
        return FlexViewHolder(view)
    }

    override fun getItemCount() = beans.size

    override fun onBindViewHolder(holder: FlexViewHolder, position: Int) {
        holder.setText(beans[position])
    }

    class FlexViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun setText(text: String) {
            (view as TextView).text = text
        }
    }
}