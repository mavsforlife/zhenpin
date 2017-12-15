package com.cang.zhenpin.zhenpincang.ui.photoview;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.base.IntentFlag;
import com.cang.zhenpin.zhenpincang.glide.GlideApp;
import com.cang.zhenpin.zhenpincang.widget.ZoomImageView;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private LinearLayout mDotLayout;
    private List<ImageView> mDots;

    private List<String> mList;
    private int mInitPos;
    private int mLastPos;

    public static Intent createIntent(Context context, ArrayList<String> list, int position) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putStringArrayListExtra(IntentFlag.IMAGE_URL_LIST, list);
        intent.putExtra(IntentFlag.IMAGE_POSITION, position);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo_view);

        mList = getIntent().getStringArrayListExtra(IntentFlag.IMAGE_URL_LIST);
        mInitPos = getIntent().getIntExtra(IntentFlag.IMAGE_POSITION, IntentFlag.IMAGE_DEFAULT_POSITION);
        mViewPager = findViewById(R.id.view_pager);
        mDotLayout = findViewById(R.id.dot);
        initPhoto();
    }

    private void initPhoto() {
        mDots = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            ZoomImageView imageView = new ZoomImageView(this);
            GlideApp.with(this)
                    .asBitmap()
                    .load(mList.get(i))
                    .into(imageView);

            if (mList.size() > 1) {
                ImageView dot = new ImageView(this);
                dot.setPadding(10, 10, 10, 10);
                if (i == mInitPos) {
                    dot.setImageResource(R.drawable.dot_now);
                } else {
                    dot.setImageResource(R.drawable.dot_page);
                }
                mDots.add(dot);
                mDotLayout.addView(dot);
            }
        }
        mLastPos = mInitPos;
        mViewPager.setAdapter(new Adapter());
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(mInitPos);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == mLastPos || mList.size() <= 1) return;
        mDots.get(mLastPos).setImageResource(R.drawable.dot_page);
        mDots.get(position).setImageResource(R.drawable.dot_now);
        mLastPos = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class Adapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String imagePath = mList.get(position);
            View v = LayoutInflater.from(PhotoViewActivity.this).inflate(R.layout.item_pager_image, container, false);
            ZoomImageView imageView = v.findViewById(R.id.iv_zoom);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoViewActivity.this.onBackPressed();
                }
            });
            GlideApp.with(PhotoViewActivity.this)
                    .asBitmap()
                    .load(imagePath)
                    .into(imageView);

            container.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
