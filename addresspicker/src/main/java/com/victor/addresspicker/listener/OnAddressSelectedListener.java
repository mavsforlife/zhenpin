package com.victor.addresspicker.listener;


import com.victor.addresspicker.model.CityModel;
import com.victor.addresspicker.model.County;
import com.victor.addresspicker.model.ProvinceModel;
import com.victor.addresspicker.model.Street;

public interface OnAddressSelectedListener {
    // 获取地址完成回调
    void onAddressSelected(ProvinceModel province, CityModel city, County county, Street street);
    // 选取省份完成回调
    void onProvinceSelected(ProvinceModel province);
    // 选取城市完成回调
    void onCitySelected(CityModel city);
    // 选取区/县完成回调
    void onCountySelected(County county);
}
