package com.renyu.androidcommonlibrary.activity

import android.content.Intent
import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.SpanUtils
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.baseact.BaseActivity
import kotlinx.android.synthetic.main.activtiy_spannablestring.*
import java.util.regex.Pattern

/**
 * Created by renyu on 2018/2/5.
 */
class SpannableStringActivtiy : BaseActivity() {

    override fun initParams() {

    }

    override fun initViews() = R.layout.activtiy_spannablestring

    override fun loadData() {
        val temp: ArrayList<SpnnableStringRange> by lazy {
            ArrayList<SpnnableStringRange>()
        }
        val items: ArrayList<SpnnableStringRange> by lazy {
            ArrayList<SpnnableStringRange>()
        }

        var text = "哈http://www.baidu.com, 15895886064, http://www.google.com"
        temp.addAll(check(text, "\\d{3}-\\d{8}|\\d{4}-\\d{7}|\\d{11}", ClickType.PHONE))
        temp.addAll(
            check(
                text,
                "[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&][%]]*",
                ClickType.WEB
            )
        )
        // 进行排序
        while (temp.size != 0) {
            var small = 0
            temp.forEachIndexed { index, _ ->
                if (temp[small].start > temp[index].start) {
                    small = index
                }
            }
            items.add(temp[small])
            temp.removeAt(small)
        }

        val spanUtils = SpanUtils()
        items.forEachIndexed { index, spnnableStringRange ->
            if (index == 0) {
                spanUtils.append(text.substring(0, spnnableStringRange.start))
                    .append(text.substring(spnnableStringRange.start, spnnableStringRange.end))
                    .setForegroundColor(Color.BLUE)
                    .setClickSpan(
                        HtmlClick(
                            spnnableStringRange.string,
                            this@SpannableStringActivtiy
                        )
                    )
                if (spnnableStringRange.type == ClickType.WEB) {
                    spanUtils.setClickSpan(
                        HtmlClick(
                            spnnableStringRange.string,
                            this@SpannableStringActivtiy
                        )
                    )
                } else if (spnnableStringRange.type == ClickType.PHONE) {
                    spanUtils.setClickSpan(
                        PhoneClick(
                            spnnableStringRange.string,
                            this@SpannableStringActivtiy
                        )
                    )
                }
            } else {
                spanUtils.append(text.substring(items[index - 1].end, spnnableStringRange.start))
                    .append(text.substring(spnnableStringRange.start, spnnableStringRange.end))
                    .setForegroundColor(Color.BLUE)
                if (spnnableStringRange.type == ClickType.WEB) {
                    spanUtils.setClickSpan(
                        HtmlClick(
                            spnnableStringRange.string,
                            this@SpannableStringActivtiy
                        )
                    )
                } else if (spnnableStringRange.type == ClickType.PHONE) {
                    spanUtils.setClickSpan(
                        PhoneClick(
                            spnnableStringRange.string,
                            this@SpannableStringActivtiy
                        )
                    )
                }
                if (index == items.size - 1) {
                    spanUtils.append(text.substring(spnnableStringRange.end, text.length))
                }
            }
        }
        tv_spannable.movementMethod = LinkMovementMethod.getInstance()
        tv_spannable.text = spanUtils.create()
    }

    override fun setStatusBarColor() = 0

    override fun setStatusBarTranslucent() = 1

    private fun check(
        text: String,
        regex: String,
        type: ClickType
    ): ArrayList<SpnnableStringRange> {
        val items = ArrayList<SpnnableStringRange>()
        val pattern = Pattern.compile(regex)
        val matcherWeb = pattern.matcher(text)
        while (matcherWeb.find()) {
            items.add(
                SpnnableStringRange(
                    matcherWeb.start(),
                    matcherWeb.end(),
                    matcherWeb.group(),
                    type
                )
            )
        }
        return items
    }
}

data class SpnnableStringRange(
    var start: Int,
    var end: Int,
    val string: String,
    val type: ClickType
)

class HtmlClick(val url: String, val activity: AppCompatActivity) : ClickableSpan() {
    override fun onClick(widget: View) {
        val intent = Intent(activity, MyX5WebActivity::class.java)
        intent.putExtra("url", url)
        activity.startActivity(intent)
    }
}

class PhoneClick(val phone: String, val activity: AppCompatActivity) : ClickableSpan() {
    override fun onClick(widget: View) {

    }
}

enum class ClickType {
    PHONE,
    WEB
}