package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.net.Uri
import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.alibaba.android.vlayout.layout.RangeGridLayoutHelper
import com.alibaba.android.vlayout.layout.SingleLayoutHelper
import com.alibaba.android.vlayout.layout.StickyLayoutHelper
import com.bigkoo.convenientbanner.ConvenientBanner
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.adapter.vlayout.*
import com.renyu.commonlibrary.baseact.BaseActivity
import java.util.*
import kotlin.collections.ArrayList


class Vlayout2Activity : BaseActivity() {
    private val manager: VirtualLayoutManager by lazy {
        VirtualLayoutManager(this)
    }
    private val delegateAdapter: DelegateAdapter by lazy {
        DelegateAdapter(manager, false)
    }

    private val adapters: MutableList<DelegateAdapter.Adapter<*>> by lazy {
        LinkedList<DelegateAdapter.Adapter<*>>()
    }

    private val bannerBeans: ArrayList<Uri> by lazy {
        ArrayList<Uri>()
    }
    private val marqueeBeans: ArrayList<String> by lazy {
        ArrayList<String>()
    }
    private val gridBeans: ArrayList<String> by lazy {
        ArrayList<String>()
    }
    private val horizontalBeans: ArrayList<String> by lazy {
        ArrayList<String>()
    }
    private val stickyBeans: ArrayList<String> by lazy {
        ArrayList<String>()
    }
    private val itemBeans: ArrayList<String> by lazy {
        ArrayList<String>()
    }

    private var cbScrollrv: ConvenientBanner<Uri>? = null

    override fun initParams() {
        val rv_vlayout = findViewById<RecyclerView>(R.id.rv_vlayout)
        val swipy_vlayout = findViewById<SwipyRefreshLayout>(R.id.swipy_vlayout)
        rv_vlayout.layoutManager = manager
        val pool = RecyclerView.RecycledViewPool()
        pool.setMaxRecycledViews(0, 20)
        rv_vlayout.setRecycledViewPool(pool)
        rv_vlayout.adapter = delegateAdapter
        swipy_vlayout.setOnRefreshListener {
            if (it == SwipyRefreshLayoutDirection.TOP) {
                Handler().postDelayed({
                    bannerBeans.add(Uri.parse("http://a.hiphotos.baidu.com/image/pic/item/0b7b02087bf40ad15a962c0b592c11dfa8ecceec.jpg"))
                    (adapters[0] as BannerAdapter).update()
                    adapters[0].notifyDataSetChanged()

                    marqueeBeans.clear()
                    marqueeBeans.add("11. 大家好，我是孙福生。\n12. 欢迎大家关注我哦！")
                    marqueeBeans.add("13. GitHub帐号：sfsheng0322 \n14. 新浪微博：孙福生微博")
                    marqueeBeans.add("15. 个人博客：sunfusheng.com \n16. 微信公众号：孙福生")
                    (adapters[1] as MarqueeAdapter).update()
                    adapters[1].notifyDataSetChanged()

                    gridBeans.clear()
                    gridBeans.addAll(getBeans(30))
                    adapters[2].notifyDataSetChanged()

                    horizontalBeans.clear()
                    horizontalBeans.addAll(getBeans(6))
                    (adapters[3] as HorizontalAdapter).update()
                    adapters[3].notifyDataSetChanged()

                    itemBeans.clear()
                    itemBeans.addAll(getBeans(15))
                    adapters[5].notifyDataSetChanged()

                    swipy_vlayout.isRefreshing = false
                }, 2000)
            } else {
                Handler().postDelayed({
                    itemBeans.addAll(getBeans(15))
                    adapters[5].notifyDataSetChanged()

                    swipy_vlayout.isRefreshing = false
                }, 2000)
            }
        }
    }

    override fun initViews() = R.layout.activity_vlayout

    override fun loadData() {
        bannerBeans.add(Uri.parse("http://f.hiphotos.baidu.com/image/pic/item/8d5494eef01f3a29f863534d9725bc315d607c8e.jpg"))
        bannerBeans.add(Uri.parse("http://a.hiphotos.baidu.com/image/h%3D300/sign=a62e824376d98d1069d40a31113eb807/838ba61ea8d3fd1fc9c7b6853a4e251f94ca5f46.jpg"))
        bannerBeans.add(Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504161160133&di=bd17c2859efa779ddca3b8ab1e1acb68&imgtype=0&src=http%3A%2F%2Fguangdong.sinaimg.cn%2F2014%2F0508%2FU10729P693DT20140508144234.jpg"))

        marqueeBeans.add("1. 大家好，我是孙福生。\n2. 欢迎大家关注我哦！")
        marqueeBeans.add("3. GitHub帐号：sfsheng0322 \n4. 新浪微博：孙福生微博")
        marqueeBeans.add("5. 个人博客：sunfusheng.com \n6. 微信公众号：孙福生")

        gridBeans.addAll(getBeans(30))

        horizontalBeans.addAll(getBeans())

        stickyBeans.add("ABC")

        itemBeans.addAll(getBeans(15))

        adapters.add(BannerAdapter(bannerBeans, SingleLayoutHelper(), object : BannerAdapter.BannerAdapterListener {
            override fun updateBannerView(view: ConvenientBanner<Uri>) {
                cbScrollrv = view
                if (!cbScrollrv!!.isTurning) {
                    cbScrollrv!!.post {
                        cbScrollrv!!.startTurning(4000)
                    }
                }
            }
        }))
        adapters.add(MarqueeAdapter(marqueeBeans, SingleLayoutHelper()))
        val layoutHelper = RangeGridLayoutHelper(4)
        layoutHelper.setAutoExpand(false)
        adapters.add(GridAdapter(gridBeans, layoutHelper))
        adapters.add(HorizontalAdapter(horizontalBeans, SingleLayoutHelper()))
        adapters.add(HeaderViewAdapter(this, StickyLayoutHelper(), stickyBeans))
        adapters.add(ItemViewAdapter(this, LinearLayoutHelper(), itemBeans))
        delegateAdapter.setAdapters(adapters)
    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0

    override fun onResume() {
        super.onResume()
        cbScrollrv?.post {
            cbScrollrv?.startTurning(4000)
        }
    }

    override fun onPause() {
        super.onPause()
        cbScrollrv?.stopTurning()
    }

    private fun getBeans(num: Int = 1): ArrayList<String> {
        val linearLayoutBeans = ArrayList<String>()
        val random = Random()
        for (i in 0 until 5 + random.nextInt(num)) {
            linearLayoutBeans.add("" + i)
        }
        return linearLayoutBeans
    }
}