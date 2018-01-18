package com.renyu.androidcommonlibrary.fragment

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.basefrag.BaseFragment
import kotlinx.android.synthetic.main.fragment_empty.*

/**
 * Created by renyu on 2018/1/18.
 */
class EmptyFragment : BaseFragment() {
    val lists: ArrayList<Fragment> by lazy {
        ArrayList<Fragment>()
    }

    override fun initParams() {
        lists.add(ViewPagerFragment())
        lists.add(ViewPagerFragment())
        lists.add(ViewPagerFragment())
        lists.add(ViewPagerFragment())
        vp_empty.adapter = FragmentViewPagerAdapter(childFragmentManager)
    }

    override fun initViews(): Int {
        return R.layout.fragment_empty
    }

    override fun loadData() {

    }

    inner class FragmentViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        override fun getItem(position: Int) = lists[position]

        override fun getCount() = lists.size
    }
}