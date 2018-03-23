package com.renyu.commonlibrary.basefrag;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.renyu.commonlibrary.network.OKHttpHelper;
import com.renyu.commonlibrary.network.Retrofit2Utils;

import butterknife.ButterKnife;
import retrofit2.Retrofit;

/**
 * Created by renyu on 15/12/3.
 */
public abstract class BaseFragment extends Fragment {
    public abstract void initParams();
    public abstract int initViews();
    public abstract void loadData();

    public View view=null;

    public OKHttpHelper httpHelper;
    public Retrofit retrofit=null;

    // 不需要再getActivity()了
    public Context context;

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    protected void onAttachToContext(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view==null) {
            view=LayoutInflater.from(getActivity()).inflate(initViews(), container, false);
            ButterKnife.bind(this, view);

            httpHelper=OKHttpHelper.getInstance();
            retrofit = Retrofit2Utils.getBaseRetrofit();
        }
        ViewGroup parent= (ViewGroup) view.getParent();
        if (parent!=null) {
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initParams();
        loadData();
    }
}
