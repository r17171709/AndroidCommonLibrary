package com.renyu.androidcommonlibrary.fragment

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.adapter.ViewPagerFragmentAdapter
import com.renyu.commonlibrary.basefrag.BaseFragment
import com.renyu.commonlibrary.commonutils.BarUtils
import kotlinx.android.synthetic.main.fragment_empty.*

/**
 * Created by renyu on 2018/1/18.
 */
class ColorFragment : BaseFragment() {
    private val lists: ArrayList<Int> by lazy {
        ArrayList<Int>()
    }

    private val adapter by lazy {
        FragmentViewPagerAdapter()
    }

    override fun initParams() {
        lists.add(Color.RED)
        lists.add(Color.GRAY)
        lists.add(Color.YELLOW)
        lists.add(Color.BLUE)
        lists.add(Color.CYAN)
        vp_empty.adapter = adapter
        btn_empty_delete.setOnClickListener {
            lists.removeAt(0)
            adapter.updateData(lists)
        }
        vp_empty.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position % 2 == 0) BarUtils.setDark(context as AppCompatActivity) else BarUtils.setWhite(
                    context as AppCompatActivity
                )
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    override fun initViews(): Int {
        return R.layout.fragment_empty
    }

    override fun loadData() {

    }

    inner class FragmentViewPagerAdapter() : ViewPagerFragmentAdapter(childFragmentManager, lists)
}