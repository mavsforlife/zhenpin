/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.cang.zhenpin.zhenpincang.pref;

import android.content.SharedPreferences;


public abstract class BasePreferences {

    public abstract SharedPreferences getSharePreferences() ;

    public  void putString(String key, String value) {
        getSharePreferences().edit().putString(key, value).commit();
    }

    public String getString(String key, String defaultValue) {
        return getSharePreferences().getString(key, defaultValue);
    }

    public  boolean putInt(String key, int value) {
        return getSharePreferences().edit().putInt(key, value).commit();
    }

    public  int getInt(String key, int defaultValue) {
        return getSharePreferences().getInt(key, defaultValue);
    }

    public  boolean putBoolean(String key, boolean value) {
        return getSharePreferences().edit().putBoolean(key, value).commit();
    }

    public  boolean getBoolean(String key, boolean defaultValue) {
        return getSharePreferences().getBoolean(key, defaultValue);
    }

    public  boolean putLong(String key, long value) {
        return getSharePreferences().edit().putLong(key, value).commit();
    }

    public  long getLong(String key, long defaultValue) {
        return getSharePreferences().getLong(key, defaultValue);
    }

    public  void putFloat(String key, float value) {
        getSharePreferences().edit().putFloat(key, value).commit();
    }

    public  float getFloat(String key, float defaultValue) {
        return getSharePreferences().getFloat(key, defaultValue);
    }

    public void clear(){
        getSharePreferences().edit().clear().commit();
    }

    public void remove(String key){
        getSharePreferences().edit().remove(key).commit();
    }

}
