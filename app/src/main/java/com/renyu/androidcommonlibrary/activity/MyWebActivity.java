package com.renyu.androidcommonlibrary.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.renyu.androidcommonlibrary.R;
import com.renyu.androidcommonlibrary.impl.WebAppInterface;
import com.renyu.commonlibrary.web.activity.WebActivity;
import com.renyu.commonlibrary.web.impl.IWebApp;

/**
 * Created by Administrator on 2017/9/13.
 */

public class MyWebActivity extends WebActivity {

    ImageButton ib_nav_right;

    @Override
    public WebView getWebView() {
        return findViewById(R.id.web_webview);
    }

    @Override
    public TextView getTitleView() {
        return findViewById(R.id.tv_nav_title);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        ib_nav_right = findViewById(R.id.ib_nav_right);
        ib_nav_right.setImageResource(R.mipmap.ic_launcher);
        ib_nav_right.setOnClickListener(v -> {
            IWebApp impl=getIntent().getParcelableExtra("IWebApp");
            ((WebAppInterface) impl).call1(123);
        });
        initViews();
    }
}
