package com.renyu.androidcommonlibrary.activity

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.adapter.RecyclerView2Adapter
import com.renyu.androidcommonlibrary.bean.Teacher2
import com.renyu.androidcommonlibrary.databinding.ActivityRecyclerviewBinding

class RecyclerViewActivity : AppCompatActivity() {

    private val teacher2: ArrayList<Teacher2> by lazy {
        ArrayList<Teacher2>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewDataBinding = DataBindingUtil.setContentView<ActivityRecyclerviewBinding>(this, R.layout.activity_recyclerview)
        viewDataBinding.adapter = RecyclerView2Adapter(teacher2)

        for (i in 0..30) {
            teacher2.add(Teacher2(ObservableField("Hello$i"), ObservableField(i)))
        }
        viewDataBinding.adapter?.notifyDataSetChanged()
    }
}