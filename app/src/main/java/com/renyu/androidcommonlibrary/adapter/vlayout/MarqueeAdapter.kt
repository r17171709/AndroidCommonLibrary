package com.renyu.androidcommonlibrary.adapter.vlayout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.gongwen.marqueen.MarqueeFactory
import com.gongwen.marqueen.MarqueeView
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.activity.NoticeMF

class MarqueeAdapter(private val beans: List<String>, private val layoutHelper: LayoutHelper) : DelegateAdapter.Adapter<MarqueeAdapter.MarqueeViewHolder>() {
    private var update = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarqueeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_vlayout_marquee, parent, false)
        val marqueeScrollrv = view.findViewById<MarqueeView<TextView, String>>(R.id.marquee_scrollrv)
        val marqueeFactory1 = NoticeMF(parent.context)
        marqueeFactory1.data = beans
        marqueeScrollrv.setMarqueeFactory(marqueeFactory1)
        marqueeScrollrv.setOnItemClickListener { mView, mData, mPosition -> Toast.makeText(mView.context, mData.toString(), Toast.LENGTH_SHORT).show() }
        marqueeScrollrv.startFlipping()
        return MarqueeViewHolder(view)
    }

    override fun getItemCount() = 1

    override fun onCreateLayoutHelper() = layoutHelper

    override fun onBindViewHolder(holder: MarqueeViewHolder, position: Int) {
        if (update) {
            update = false

            val marqueeScrollrv = holder.rootView.findViewById<MarqueeView<TextView, String>>(R.id.marquee_scrollrv)
            val factory = marqueeScrollrv.javaClass.getDeclaredField("factory")
            factory.isAccessible = true
            (factory.get(marqueeScrollrv) as MarqueeFactory<TextView, String>).data = beans
        }
    }

    class MarqueeViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView)

    override fun onViewDetachedFromWindow(holder: MarqueeViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.rootView.findViewById<MarqueeView<TextView, String>>(R.id.marquee_scrollrv).stopFlipping()
    }

    fun update() {
        update = true
    }
}