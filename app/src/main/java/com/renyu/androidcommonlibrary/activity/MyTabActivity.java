package com.renyu.androidcommonlibrary.activity;

import android.graphics.Color;
import androidx.fragment.app.Fragment;
import com.renyu.androidcommonlibrary.R;
import com.renyu.androidcommonlibrary.fragment.TabFragment;
import com.renyu.commonlibrary.baseact.BaseTabActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/10.
 */
public class MyTabActivity extends BaseTabActivity {
    @Override
    public ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(TabFragment.getInstance(Color.BLACK));
        fragments.add(TabFragment.getInstance(Color.RED));
        fragments.add(TabFragment.getInstance(Color.BLUE));
        fragments.add(TabFragment.getInstance(Color.YELLOW));
        return fragments;
    }

    @Override
    public ArrayList<String> getTags() {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("one");
        tags.add("two");
        tags.add("three");
        tags.add("four");
        return tags;
    }

    @Override
    public int getContainerLayout() {
        return R.id.main_framelayout;
    }

    @Override
    public ArrayList<Integer> getSelPic() {
        ArrayList<Integer> pics = new ArrayList<>();
        pics.add(R.mipmap.ic_index_home_sel);
        pics.add(R.mipmap.ic_index_customer_sel);
        pics.add(R.mipmap.ic_index_message_sel);
        pics.add(R.mipmap.ic_index_my_sel);
        return pics;
    }

    @Override
    public int getSelColor() {
        return Color.parseColor("#009dff");
    }

    @Override
    public ArrayList<Integer> getNorPic() {
        ArrayList<Integer> pics = new ArrayList<>();
        pics.add(R.mipmap.ic_index_home_nor);
        pics.add(R.mipmap.ic_index_customer_nor);
        pics.add(R.mipmap.ic_index_message_nor);
        pics.add(R.mipmap.ic_index_my_nor);
        return pics;
    }

    @Override
    public int getNorColor() {
        return Color.parseColor("#666666");
    }

    @Override
    public ArrayList<Integer> getTextViews() {
        ArrayList<Integer> textViews = new ArrayList<>();
        textViews.add(R.id.index_text);
        textViews.add(R.id.fav_text);
        textViews.add(R.id.message_text);
        textViews.add(R.id.my_text);
        return textViews;
    }

    @Override
    public ArrayList<Integer> getImageViews() {
        ArrayList<Integer> imageViews = new ArrayList<>();
        imageViews.add(R.id.index_image);
        imageViews.add(R.id.fav_image);
        imageViews.add(R.id.message_image);
        imageViews.add(R.id.my_image);
        return imageViews;
    }

    @Override
    public void initParams() {
        findViewById(R.id.index_layout).setOnClickListener(v -> switchFragment("one"));
        findViewById(R.id.fav_layout).setOnClickListener(v -> switchFragment("two"));
        findViewById(R.id.message_layout).setOnClickListener(v -> switchFragment("three"));
        findViewById(R.id.my_layout).setOnClickListener(v -> switchFragment("four"));
    }

    @Override
    public int initViews() {
        return R.layout.activity_index;
    }

    @Override
    public void loadData() {
        switchFragment(getTags().get(0));
    }

    @Override
    public int setStatusBarColor() {
        return Color.BLACK;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }
}
