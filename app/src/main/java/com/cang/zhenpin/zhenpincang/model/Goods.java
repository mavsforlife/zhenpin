package com.cang.zhenpin.zhenpincang.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;

import java.util.List;

/**
 * Created by victor on 2017/11/14.
 * Email: wwmdirk@gmail.com
 */
@Keep
public class Goods implements Parcelable {

    private String message;
    private List<Image> mImages;
    private boolean isCollapsed = true;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Image> getImages() {
        return mImages;
    }

    public void setImages(List<Image> images) {
        mImages = images;
    }

    public boolean isCollapsed() {
        return isCollapsed;
    }

    public void setCollapsed(boolean collapsed) {
        isCollapsed = collapsed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeTypedList(this.mImages);
        dest.writeByte(this.isCollapsed ? (byte) 1 : (byte) 0);
    }

    public Goods() {
    }

    protected Goods(Parcel in) {
        this.message = in.readString();
        this.mImages = in.createTypedArrayList(Image.CREATOR);
        this.isCollapsed = in.readByte() != 0;
    }

    public static final Creator<Goods> CREATOR = new Creator<Goods>() {
        @Override
        public Goods createFromParcel(Parcel source) {
            return new Goods(source);
        }

        @Override
        public Goods[] newArray(int size) {
            return new Goods[size];
        }
    };
}
