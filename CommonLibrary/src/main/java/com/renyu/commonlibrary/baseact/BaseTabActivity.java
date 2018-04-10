package com.renyu.commonlibrary.baseact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/10.
 */
public abstract class BaseTabActivity extends BaseActivity {

    public abstract ArrayList<Fragment> getFragments();
    public abstract ArrayList<String> getTags();
    public abstract int getContainerLayout();
    public abstract ArrayList<Integer> getSelPic();
    public abstract int getSelColor();
    public abstract ArrayList<Integer> getNorPic();
    public abstract int getNorColor();
    public abstract ArrayList<Integer> getTextViews();
    public abstract ArrayList<Integer> getImageViews();

    String currentTag = "one";
    Fragment currentFragment = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            for (int i = 0; i < manager.getFragments().size(); i++) {
                transaction.hide(manager.getFragments().get(i));
            }
            transaction.commit();

            currentTag = savedInstanceState.getString("currentTag");
            currentFragment = manager.findFragmentByTag(currentTag);

            switchFragment(currentTag);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("currentTag", currentTag);
    }

    public void switchFragment(String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        for (int i = 0; i < getTags().size(); i++) {
            if (tag.equals(getTags().get(i))) {
                Fragment fragment = manager.findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = getFragments().get(i);
                    transaction.add(getContainerLayout(), fragment, tag);
                }
                else {
                    transaction.show(fragment);
                }
                currentFragment = fragment;
                break;
            }
        }
        transaction.commitAllowingStateLoss();
        currentTag = tag;

        for (int i = 0; i < getImageViews().size(); i++) {
            ImageView imageView = findViewById(getImageViews().get(i));
            imageView.setImageResource(getNorPic().get(i));
            TextView textView = findViewById(getTextViews().get(i));
            textView.setTextColor(getNorColor());
        }
        for (int i = 0; i < getTags().size(); i++) {
            if (tag.equals(getTags().get(i))) {
                ImageView imageView = findViewById(getImageViews().get(i));
                imageView.setImageResource(getSelPic().get(i));
                TextView textView = findViewById(getTextViews().get(i));
                textView.setTextColor(getSelColor());
                break;
            }
        }
    }
}
