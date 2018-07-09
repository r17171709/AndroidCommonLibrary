package com.renyu.androidcommonlibrary.adapter

import com.renyu.androidcommonlibrary.BR
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.bean.Teacher2
import com.renyu.androidcommonlibrary.databinding.AdapterRecyclerviewBinding
import com.renyu.commonlibrary.adapter.BaseAdapter
import java.util.*

/**
 * Created by Administrator on 2018/7/9.
 */
class RecyclerView2Adapter(private val beans: ArrayList<Teacher2>) : BaseAdapter<AdapterRecyclerviewBinding, Teacher2>(beans) {
    override fun initViews() = R.layout.adapter_recyclerview

    override fun dataVariableId() = BR.teacher
}