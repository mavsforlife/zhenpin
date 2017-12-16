package com.cang.zhenpin.zhenpincang.widget.ninegridlayout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.glide.GlideApp;
import com.cang.zhenpin.zhenpincang.ui.photoview.PhotoViewActivity;
import com.cang.zhenpin.zhenpincang.util.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 2017/11/28.
 * Email: wwmdirk@gmail.com
 */

public class ImgGridView extends AbstractNineGridLayout<List<String>> {
    private ImageView[] imageViews;

    public ImgGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void fill() {
        fill(R.layout.item_image_grid);
        imageViews = findInChildren(R.id.image, ImageView.class);
    }

    @Override
    public void render(final List<String> data) {
        setSingleModeSize(DeviceUtil.getScreenWidthPixel(getContext()) / 3, DeviceUtil.getScreenHeightPixel(getContext()) / 3);
        setDisplayCount(data.size());
        if (data.size() == 1) {
            String url = data.get(0);
            ImageView imageView = imageViews[0];
            GlideApp.with(getContext())
                    .load(url)
                    .fitCenter()
                    .placeholder(R.color.grayLighter)
                    .error(R.color.grayLighter)
                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getContext().startActivity(PhotoViewActivity.createIntent(getContext(), (ArrayList<String>) data, 0));
                    ((Activity)getContext()).overridePendingTransition(0, 0);
                }
            });
        } else {
            for (int i = 0; i < data.size(); i++) {
                String url = data.get(i);
                GlideApp.with(getContext())
                        .load(url)
                        .placeholder(R.color.grayLighter)
                        .error(R.color.grayLighter)
                        .into(imageViews[i]);
                ImageView imageView = imageViews[i];
                final int finalI = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContext().startActivity(PhotoViewActivity.createIntent(getContext(), (ArrayList<String>) data, finalI));
                        ((Activity)getContext()).overridePendingTransition(0, 0);
                    }
                });
            }
        }
    }
}