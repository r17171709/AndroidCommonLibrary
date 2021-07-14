package com.renyu.androidcommonlibrary.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.renyu.androidcommonlibrary.databinding.FragmentTabBinding;

/**
 * Created by Administrator on 2018/4/10.
 */
public class TabFragment extends Fragment {
    private FragmentTabBinding viewBinding;

    // Fragment监听Activity的onBackPressed
    private OnBackPressedCallback onBackPressedCallback;

    public static TabFragment getInstance(int color) {
        TabFragment tabFragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("color", color);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = FragmentTabBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewBinding.layoutTab.setBackgroundColor(getArguments().getInt("color"));

        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // 拦截返回事件
                Log.d("TAGTAGTAG", TabFragment.this + "handleOnBackPressed");
                setEnabled(getChildFragmentManager().getBackStackEntryCount() > 0);
                if (isEnabled()) {
                    getChildFragmentManager().popBackStackImmediate();
                } else {
                    requireActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            }
        };
        // 此 callback 仅当 TabFragment 至少是 Started 状态下调用
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), onBackPressedCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 在Fragment中使用View Binding需要多加注意，如果使用不当它会引发内存泄漏，如果你没有在onDestroy中将view置空，那么它就不会从内存中清除
        viewBinding = null;
        onBackPressedCallback.remove();
    }
}
