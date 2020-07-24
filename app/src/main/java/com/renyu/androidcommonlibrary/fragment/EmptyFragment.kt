package com.renyu.androidcommonlibrary.fragment

import android.graphics.Color
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.adapter.ViewPagerFragmentAdapter
import com.renyu.commonlibrary.basefrag.BaseFragment
import kotlinx.android.synthetic.main.fragment_empty.*

/**
 * Created by renyu on 2018/1/18.
 */
class EmptyFragment : BaseFragment() {
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
    }

    override fun initViews(): Int {
        return R.layout.fragment_empty
    }

    override fun loadData() {

    }

    inner class FragmentViewPagerAdapter() : ViewPagerFragmentAdapter(childFragmentManager, lists)
}