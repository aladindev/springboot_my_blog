package com.aladin.springbootstudy.common;

import java.math.BigDecimal;

public interface CommonUtils {

    static final String OAUTH_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    static final String GET_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";



    public default String roundUp(String asset) {
        return new BigDecimal(asset).setScale(3, BigDecimal.ROUND_UP).toString();
    }
    public default String roundUp(Double asset) {
        return new BigDecimal(asset).setScale(3, BigDecimal.ROUND_UP).toString();
    }
    public default String roundUp(BigDecimal asset) {
        return asset.setScale(3, BigDecimal.ROUND_UP).toString();
    }

    public default String addComma(String asset) {

        boolean minus = false;
        if(asset.indexOf("-") > -1) {
            minus = true;
            asset = asset.substring(1);
        }

        StringBuilder sb = new StringBuilder(asset).reverse();
        String str = sb.toString();
        sb.setLength(0);
        for(int i = 0 ; i < str.length() ; i++) {
            if(i == 0) {
                sb.append(str.charAt(i));
            } else {
                if(i%3==0) sb.append("," + str.charAt(i));
                else sb.append(str.charAt(i));
            }
        }
        return minus ? "-" + sb.reverse().toString() : sb.reverse().toString();
    }

}
