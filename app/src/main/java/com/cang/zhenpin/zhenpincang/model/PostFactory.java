package com.cang.zhenpin.zhenpincang.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoDongping on 5/12/16.
 */
public class PostFactory {
    private static final String CONTENT = "{\"ID\":1662,\"OpenID\":\"oSiTVwWcL7sSppCkJ6IHmI3_Fn_E\",\"UserID\":\"\\u5609\\u9c7c\",\"Password\":null,\"Token\":\"\",\"Headimgurl\":\"http:\\/\\/wx.qlogo.cn\\/mmopen\\/vi_32\\/qYz01HpC4goqEnE6jKK2keMqYQtCB9Y7fMUV2gZCsoSOpCyRX7s60ObYRBN6sviaENBV6rrlAPaZZp46X8bKpwg\\/0\",\"Principal\":\"-\",\"Sex\":1,\"Email\":null,\"Mobile\":null,\"Remark\":null,\"RegistTime\":\"2017-12-08 16:30:08\",\"IsDelete\":\"0\",\"IsAdmin\":\"0\",\"Usertype\":1,\"AppDate\":null,\"CheckDate\":null,\"IsVideo\":null,\"Weixin_Name\":null,\"IsLocked\":null,\"UserCode\":\"001662\"}";

    public static UserInfo fake() {
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(CONTENT, UserInfo.class);
        return userInfo;
    }
}
