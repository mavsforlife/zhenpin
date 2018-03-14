package com.cang.zhenpin.zhenpincang.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 2018/3/11.
 * Email: wwmdirk@gmail.com
 */

@Keep
public class AddressList implements Parcelable {


    /**
     * countall : 3
     * pagesize : 10
     * pageshow :
     * pageall : 1
     * pagenow : 1
     */

    private int countall;
    private int pagesize;
    private String pageshow;
    private int pageall;
    private int pagenow;

    @SerializedName("brand")
    private List<Address> mAddresses;

    public int getCountall() {
        return countall;
    }

    public void setCountall(int countall) {
        this.countall = countall;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public String getPageshow() {
        return pageshow;
    }

    public void setPageshow(String pageshow) {
        this.pageshow = pageshow;
    }

    public int getPageall() {
        return pageall;
    }

    public void setPageall(int pageall) {
        this.pageall = pageall;
    }

    public int getPagenow() {
        return pagenow;
    }

    public void setPagenow(int pagenow) {
        this.pagenow = pagenow;
    }

    public List<Address> getAddresses() {
        return mAddresses;
    }

    public void setAddresses(List<Address> addresses) {
        mAddresses = addresses;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.countall);
        dest.writeInt(this.pagesize);
        dest.writeString(this.pageshow);
        dest.writeInt(this.pageall);
        dest.writeInt(this.pagenow);
        dest.writeList(this.mAddresses);
    }

    public AddressList() {
    }

    protected AddressList(Parcel in) {
        this.countall = in.readInt();
        this.pagesize = in.readInt();
        this.pageshow = in.readString();
        this.pageall = in.readInt();
        this.pagenow = in.readInt();
        this.mAddresses = new ArrayList<Address>();
        in.readList(this.mAddresses, Address.class.getClassLoader());
    }

    public static final Parcelable.Creator<AddressList> CREATOR = new Parcelable.Creator<AddressList>() {
        @Override
        public AddressList createFromParcel(Parcel source) {
            return new AddressList(source);
        }

        @Override
        public AddressList[] newArray(int size) {
            return new AddressList[size];
        }
    };
}
