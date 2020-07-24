package com.renyu.androidcommonlibrary.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.renyu.androidcommonlibrary.fragment.ViewPagerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by renyu on 2017/8/24.
 */

public class ViewPagerFragmentAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> mFragmentList;

    public ViewPagerFragmentAdapter(FragmentManager fm, List<Integer> types) {
        super(fm);
        updateData(types);
    }

    public void updateData(List<Integer> dataList) {
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0, size = dataList.size(); i < size; i++) {
            fragments.add(new ViewPagerFragment(dataList.get(i)));
        }
        setFragmentList(fragments);
    }

    private void setFragmentList(ArrayList<Fragment> fragmentList) {
        if (this.mFragmentList != null) {
            mFragmentList.clear();
        }
        this.mFragmentList = fragmentList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.mFragmentList.size();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
}
