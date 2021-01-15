package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.fragment.ViewPagerFragment
import com.renyu.commonlibrary.baseact.BaseActivity
import kotlinx.android.synthetic.main.activity_viewpager.*

class ViewPager2Activity : BaseActivity() {
    private val colors by lazy {
        ArrayList<Int>()
    }
    private val fragments by lazy {
        ArrayList<Fragment>()
    }
    private val adapter by lazy {
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
        vp_vpdemo.adapter = adapter

        button1_vp.setOnClickListener {
            fragments.add(ViewPagerFragment(colors[index % colors.size]))
            index++
            adapter.notifyDataSetChanged()
        }

        button2_vp.setOnClickListener {
            adapter.notifyItemRemoved(fragments.size - 1)
            fragments.removeAt(fragments.size - 1)
            index--
        }

        button3_vp.setOnClickListener {
            fragments.addAll(
                colors.map {
                    ViewPagerFragment(it)
                }.reversed().toList()
            )
            adapter.notifyDataSetChanged()
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
}