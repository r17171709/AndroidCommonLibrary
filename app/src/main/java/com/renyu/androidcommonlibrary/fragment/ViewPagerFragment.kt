package com.renyu.androidcommonlibrary.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.databinding.FragmentViewpagerBinding
import com.renyu.commonlibrary.commonutils.binding

/**
 * Created by renyu on 2018/1/18.
 */
class ViewPagerFragment(val color: Int) : Fragment(R.layout.fragment_viewpager) {
    private val binding: FragmentViewpagerBinding by binding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layoutVp.setBackgroundColor(color)
    }
}