package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.fragment.ViewPagerFragment
import com.renyu.commonlibrary.baseact.BaseActivity

class ViewPager2Activity : BaseActivity() {
    private val colors by lazy {
        ArrayList<Int>()
    }
    private val fragments by lazy {
        ArrayList<Fragment>()
    }
    private val _adapter by lazy {
        ViewPagerAdapter()
    }

    private var index = 0

    override fun initParams() {
        index = colors.size
        fragments.addAll(
            colors.apply {
                add(Color.RED)
                add(Color.YELLOW)
                add(Color.GREEN)
                add(Color.BLUE)
                add(Color.CYAN)
                add(Color.WHITE)
                add(Color.BLACK)
            }.map {
                ViewPagerFragment(it)
            }.toList()
        )

        findViewById<ViewPager2>(R.id.vp_vpdemo).apply {
            // 预加载个数
            offscreenPageLimit = 1
            // 一屏多页效果
//            val rv = getChildAt(0) as RecyclerView
//            rv.apply {
//                setPadding(SizeUtils.dp2px(40f), 0, SizeUtils.dp2px(40f), 0)
//                clipToPadding = false
//            }

            adapter = _adapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }

        TabLayoutMediator(
            findViewById<TabLayout>(R.id.tab_vp),
            findViewById<ViewPager2>(R.id.vp_vpdemo),
            true
        ) { tab, position ->
            val textView = TextView(this@ViewPager2Activity)
            textView.gravity = Gravity.CENTER
            textView.text = "$position"
            tab.customView = textView
        }.attach()

        R.id.button1_vp.onClick {
            fragments.add(ViewPagerFragment(colors[index % colors.size]))
            index++
            _adapter.notifyDataSetChanged()
        }

        R.id.button2_vp.onClick {
            _adapter.notifyItemRemoved(fragments.size - 1)
            fragments.removeAt(fragments.size - 1)
            index--
        }

        R.id.button3_vp.onClick {
            fragments.addAll(
                colors.map {
                    ViewPagerFragment(it)
                }.reversed().toList()
            )
            _adapter.notifyDataSetChanged()
        }
    }

    override fun initViews() = R.layout.activity_viewpager

    override fun loadData() {

    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0

    inner class ViewPagerAdapter() : FragmentStateAdapter(this) {
        override fun getItemCount() = fragments.size

        override fun createFragment(position: Int) = fragments[position]
    }

    private fun Int.onClick(click: () -> Unit) {
        findViewById<View>(this).setOnClickListener {
            click()
        }
    }
}