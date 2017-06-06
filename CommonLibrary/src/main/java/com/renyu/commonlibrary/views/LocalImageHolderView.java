package com.renyu.commonlibrary.views;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.renyu.commonlibrary.R;

/**
 * Created by renyu on 2017/5/8.
 */

public class LocalImageHolderView implements Holder<Uri> {
    private View view;
    @Override
    public View createView(Context context) {
        view= LayoutInflater.from(context).inflate(R.layout.adapter_convenientbanner, null, false);
        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, Uri data) {
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(data).setAutoPlayAnimations(true).build();
        ((SimpleDraweeView) view).setController(draweeController);
    }
}
