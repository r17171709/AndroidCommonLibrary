package com.renyu.androidcommonlibrary.adapter.vlayout

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.bigkoo.convenientbanner.listener.OnPageChangeListener
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.views.LocalImageHolderView
import com.tencent.mars.xlog.Log

class BannerAdapter(private val beans: ArrayList<Uri>, private val layoutHelper: LayoutHelper, private val listener: BannerAdapterListener) : DelegateAdapter.Adapter<BannerAdapter.BannerViewHolder>() {
    private var update = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_vlayout_banner, parent, false)
        initData(view)
        return BannerViewHolder(view)
    }

    override fun getItemCount() = 1

    override fun onCreateLayoutHelper() = layoutHelper

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val cbScrollrv = holder.rootView.findViewById<ConvenientBanner<Uri>>(R.id.cb_scrollrv)
        listener.updateBannerView(cbScrollrv)
        if (update) {
            update = false
            initData(holder.rootView)
        }
    }

    class BannerViewHolder(val rootView: View): RecyclerView.ViewHolder(rootView)

    interface BannerAdapterListener {
        fun updateBannerView(view: ConvenientBanner<Uri>)
    }

    private fun initData(view: View) {
        val cbScrollrv = view.findViewById<ConvenientBanner<Uri>>(R.id.cb_scrollrv)
        cbScrollrv.setPages(object : CBViewHolderCreator {
            override fun createHolder(itemView: View): Holder<*> {
                return LocalImageHolderView(itemView)
            }

            override fun getLayoutId(): Int {
                return R.layout.adapter_convenientbanner
            }
        }, beans).setOnItemClickListener {

        }.onPageChangeListener = object : OnPageChangeListener {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }

            override fun onPageSelected(index: Int) {
                Log.d("ScrollRVActivity", "position:$index")
            }
        }
    }

    fun update() {
        update = true
    }
}