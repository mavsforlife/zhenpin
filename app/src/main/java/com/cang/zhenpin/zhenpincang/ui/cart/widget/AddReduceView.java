package com.cang.zhenpin.zhenpincang.ui.cart.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.widget.BackEditText;

/**
 * Created by victor on 2018/3/10.
 * Email: wwmdirk@gmail.com
 */

public class AddReduceView extends LinearLayout implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener, BackEditText.BackListener {

    private static final String TAG = AddReduceView.class.getSimpleName();

    private BackEditText mEtCount;

    private int mCount = 1;

    private int mMaxCount = 100;

    private int mTempCount;

    public AddReduceView(Context context) {
        super(context);
        init();
    }

    public AddReduceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AddReduceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AddReduceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_add_reduce_widget, this, false);
        TextView add = linearLayout.findViewById(R.id.add);
        TextView reduce = linearLayout.findViewById(R.id.reduce);
        mEtCount = linearLayout.findViewById(R.id.count);
        mEtCount.setText(String.valueOf(mCount));
        addView(linearLayout);

        add.setOnClickListener(this);
        reduce.setOnClickListener(this);
        mEtCount.addTextChangedListener(this);
        mEtCount.setOnEditorActionListener(this);
        mEtCount.setBackListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mOnCountChangeListener == null) return;
        if (v.getId() == R.id.add) {
            if (mTempCount < mMaxCount - 1) {
                mTempCount++;
                mOnCountChangeListener.onCountChange(mTempCount);
            }
        } else if (v.getId() == R.id.reduce) {
            if (mCount > 1) {
                mTempCount--;
                mOnCountChangeListener.onCountChange(mTempCount);
            }
        }
    }

    public void setCount(int count, int maxCount) {
        mCount = count;
        mTempCount = mCount;
        mEtCount.setText(String.valueOf(count));
        mMaxCount = maxCount;
    }

    public void confirmCountChange() {
        mCount = mTempCount;
        mEtCount.setText(String.valueOf(mCount));
    }

    private OnCountChangeListener mOnCountChangeListener;

    public void setOnCountChangeListener(OnCountChangeListener listener) {
        mOnCountChangeListener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s) || Integer.parseInt(s.toString()) < 1) {
            mEtCount.setText("1");
            mEtCount.setSelection(s.length());
            return;
        }

        if (Integer.parseInt(s.toString()) > mMaxCount) {
            String maxCount = String.valueOf(mMaxCount);
            mEtCount.setText(maxCount);
            mEtCount.setSelection(maxCount.length());
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE){
                    /*隐藏软键盘*/
            InputMethodManager imm = (InputMethodManager) v
                    .getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            if (imm == null) {
                return false;
            }
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(
                        v.getApplicationWindowToken(), 0);
            }
            mTempCount = Integer.parseInt(mEtCount.getText().toString());
            if (mOnCountChangeListener != null) {
                mOnCountChangeListener.onCountChange(mTempCount);
            }
            mEtCount.clearFocus();
            return true;
        } else {
            mEtCount.setText(String.valueOf(mCount));
            mEtCount.clearFocus();
            return false;
        }
    }

    @Override
    public void back(TextView textView) {
        mEtCount.setText(String.valueOf(mCount));
        mEtCount.clearFocus();
    }

    public interface OnCountChangeListener {
        void onCountChange(int count);
    }
}
