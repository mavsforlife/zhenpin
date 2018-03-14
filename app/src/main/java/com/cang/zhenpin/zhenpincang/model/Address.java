package com.cang.zhenpin.zhenpincang.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;

/**
 * Created by victor on 2018/3/10.
 * Email: wwmdirk@gmail.com
 */

@Keep
public class Address implements Parcelable {

    /**
     * ID : 6
     * UID : 1662
     * Address : 上海市
     * Mobile : 18812345678
     * Name : sss
     * ISDefault : 0
     * AddDate : 2018-03-10 23:48:16
     */

    private String ID;
    private String UID;
    private String Address;
    private String Mobile;
    private String Name;
    private int ISDefault;
    private String AddDate;

    private boolean mIsOrderAddress;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getISDefault() {
        return ISDefault;
    }

    public void setISDefault(int ISDefault) {
        this.ISDefault = ISDefault;
    }

    public String getAddDate() {
        return AddDate;
    }

    public void setAddDate(String AddDate) {
        this.AddDate = AddDate;
    }

    public boolean isOrderAddress() {
        return mIsOrderAddress;
    }

    public void setOrderAddress(boolean orderAddress) {
        mIsOrderAddress = orderAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ID);
        dest.writeString(this.UID);
        dest.writeString(this.Address);
        dest.writeString(this.Mobile);
        dest.writeString(this.Name);
        dest.writeInt(this.ISDefault);
        dest.writeString(this.AddDate);
        dest.writeByte(this.mIsOrderAddress ? (byte) 1 : (byte) 0);
    }

    public Address() {
    }

    protected Address(Parcel in) {
        this.ID = in.readString();
        this.UID = in.readString();
        this.Address = in.readString();
        this.Mobile = in.readString();
        this.Name = in.readString();
        this.ISDefault = in.readInt();
        this.AddDate = in.readString();
        this.mIsOrderAddress = in.readByte() != 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
