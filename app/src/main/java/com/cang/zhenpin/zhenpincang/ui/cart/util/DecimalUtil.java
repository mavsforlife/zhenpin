package com.cang.zhenpin.zhenpincang.ui.cart.util;

import com.cang.zhenpin.zhenpincang.model.CartBrand;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class DecimalUtil {

    /**
     * 金钱加法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);

        return b1.add(b2).toString();
    }

    /**
     * 金钱乘法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String multiply(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);

        return b1.multiply(b2).toString();
    }

    /**
     * 乘法
     *
     * @param v1    乘数
     * @param v2    被乘数
     * @param scale 小数点保留位数
     * @return
     */
    public static String multiplyWithScale(String v1, String v2, int scale) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        BigDecimal result = b1.multiply(b2);
        result = result.setScale(scale);
        return result.toString();
    }

    /**
     * 金钱除法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String divide(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);

        return b1.divide(b2).toString();
    }

    /**
     * 如果除不断，产生无限循环小数，那么就会抛出异常，解决方法就是对小数点后的位数做限制
     *
     * @param v1
     * @param v2 小数模式 ，DOWN，表示直接不做四舍五入，参考{@link RoundingMode}
     * @return
     */
    public static String divideWithRoundingDown(String v1, String v2) {
        return divideWithRoundingMode(v1, v2, RoundingMode.DOWN);
    }

    /** 选择小数部分处理方式 */
    public static String divideWithRoundingMode(String v1, String v2,
                                                RoundingMode roundingMode) {
        return divideWithRoundingModeAndScale(v1, v2, RoundingMode.DOWN, 0);
    }

    /**
     * 选择小数点后部分处理方式，以及保留几位小数
     *
     * @param v1           除数
     * @param v2           被除数
     * @param roundingMode 小数处理模式
     * @param scale        保留几位
     * @return
     */
    public static String divideWithRoundingModeAndScale(String v1, String v2,
                                                        RoundingMode roundingMode, int scale) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, roundingMode).toString();
    }

    /**
     * 金钱减法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String subtract(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);

        return b1.subtract(b2).toString();
    }

    /**
     * 保留两位小数
     * @param str
     * @return
     */
    public static String formatMoney(String str){

        try{
            Double d = Double.parseDouble(str);
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(d);
        }catch (NumberFormatException e){
            return "";
        }
    }

    public static String[] getTotlaCount(List<CartBrand> list) {
        String[] datas = new String[4];
        String money = "0";
        String count = "0";
        String isSelectAll = "0";
        String isDelSelectAll = "0";

        if (list != null && list.size() != 0) {
            for (CartBrand cartBrand : list) {
                if (cartBrand.isChecked()) {
                    String price = String.valueOf(cartBrand.getMPrice());
                    String quantity = String.valueOf(cartBrand.getMQuantity());
                    String currentPrice = multiply(price, quantity);
                    count = add(count, "1");
                    money = add(money, currentPrice);
                } else {
                    isSelectAll = "1";
                }
                if (!cartBrand.isDelChecked()) {
                    isDelSelectAll = "1";
                }
            }
        }

        datas[0] = formatMoney(money);
        datas[1] = count;
        datas[2] = isSelectAll;
        datas[3] = isDelSelectAll;
        return datas;
    }
}
