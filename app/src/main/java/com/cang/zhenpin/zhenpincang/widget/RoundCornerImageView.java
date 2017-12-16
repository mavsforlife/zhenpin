package com.cang.zhenpin.zhenpincang.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.util.DeviceUtil;


/**
 * Created by victor on 2016/8/11.
 * 圆角图片
 */
public class RoundCornerImageView extends AppCompatImageView {

    private Paint paint;
    private Context mContext;
    private static final int ZERO = 0;
    private float mRect;
    public RoundCornerImageView(Context context) {
        this(context, null);
        mContext = context;
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        mContext = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundCornerImageView, 0, 0);

        try {
            mRect = a.getDimension(R.styleable.RoundCornerImageView_rect, R.dimen.dp_4);
        }finally {
            a.recycle();
        }
    }

    /**
     * 绘制圆角矩形图片
     *
     * @author caizhiming
     */
    @Override
    protected void onDraw(Canvas canvas) {

        Path clipPath = new Path();
        int w = this.getWidth();
        int h = this.getHeight();
        clipPath.addRoundRect(new RectF(0, 0, w, h), mRect, mRect, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }


}
