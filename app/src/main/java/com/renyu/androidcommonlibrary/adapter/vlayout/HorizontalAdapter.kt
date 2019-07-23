package com.renyu.androidcommonlibrary.adapter.vlayout

import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.renyu.androidcommonlibrary.R

class HorizontalAdapter(private val beans: ArrayList<String>, private val layoutHelper: LayoutHelper) : DelegateAdapter.Adapter<HorizontalAdapter.HorizontalViewHolder>() {
    private var update = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_vlayout_horizontal, parent, false)
        initData(view)
        return HorizontalViewHolder(view)
    }

    override fun getItemCount() = 1

    override fun onCreateLayoutHelper() = layoutHelper

    override fun onBindViewHolder(holder: HorizontalViewHolder, position: Int) {
        if (update) {
            update = false

            initData(holder.rootView)
        }
    }

    private fun initData(view: View) {
        val horizontalScrollrv = view.findViewById<LinearLayout>(R.id.horizontal_scrollrv)
        horizontalScrollrv.removeAllViews()
        for (i in beans.indices) {
            val v = LayoutInflater.from(view.context).inflate(R.layout.adapter_item, null, false)
            v.setOnClickListener { Log.d("ScrollRVActivity", "点击3") }
            val tv_releaserentalsuccess_address = v.findViewById<TextView>(R.id.tv_releaserentalsuccess_address)
            tv_releaserentalsuccess_address.text = beans[i]
            horizontalScrollrv.addView(v)
        }
        Handler().post {
            ((horizontalScrollrv.parent) as HorizontalScrollView).fullScroll(ScrollView.FOCUS_UP)
        }
    }

    class HorizontalViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView)

    fun update() {
        update = true
    }
}