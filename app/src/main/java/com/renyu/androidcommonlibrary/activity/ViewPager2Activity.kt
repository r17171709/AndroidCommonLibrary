package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
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
        findViewById<ViewPager2>(R.id.vp_vpdemo).adapter = adapter

        R.id.button1_vp.onClick {
            fragments.add(ViewPagerFragment(colors[index % colors.size]))
            index++
            adapter.notifyDataSetChanged()
        }

        R.id.button2_vp.onClick {
            adapter.notifyItemRemoved(fragments.size - 1)
            fragments.removeAt(fragments.size - 1)
            index--
        }

        R.id.button3_vp.onClick {
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

    private fun Int.onClick(click: () -> Unit) {
        findViewById<View>(this).setOnClickListener {
            click()
        }
    }
}