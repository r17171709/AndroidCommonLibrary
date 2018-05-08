package com.renyu.commonlibrary.views;

import android.net.Uri;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by renyu on 2017/5/8.
 */

public class LocalImageHolderView extends Holder<Uri> {
    private View view;

    public LocalImageHolderView(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        view = itemView;
    }

    @Override
    public void updateUI(Uri data) {
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(data).setAutoPlayAnimations(true).build();
        ((SimpleDraweeView) view).setController(draweeController);
    }
}
