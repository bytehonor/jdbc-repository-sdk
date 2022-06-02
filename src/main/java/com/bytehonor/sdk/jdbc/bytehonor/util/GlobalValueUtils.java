package com.bytehonor.sdk.jdbc.bytehonor.util;

import javax.servlet.http.HttpServletRequest;

import com.bytehonor.sdk.define.bytehonor.constant.HeaderKey;
import com.bytehonor.sdk.define.bytehonor.util.StringObject;

public class GlobalValueUtils {

    public static String getFromTerminal(HttpServletRequest request) {
        String from = request.getHeader(HeaderKey.X_FROM_TERMINAL);
        if (StringObject.isEmpty(from)) {
            from = "browser";
        }
        return from;
    }

    public static String getFromIp(HttpServletRequest request) {
        String ip = request.getHeader(HeaderKey.X_REAL_IP);
        if (StringObject.isEmpty(ip) == false) {
            return ip;
        }
        ip = request.getHeader(HeaderKey.X_FORWARDED_FOR);
        if (StringObject.isEmpty(ip) == false) {
            int at = ip.indexOf(",");
            if (at < 0) {
                return ip;
            }
            return ip.substring(0, at);
        }

        return request.getRemoteAddr();
    }

    public static String getUserUuid(HttpServletRequest request) {
        String val = request.getHeader(HeaderKey.X_USER_UUID);
        if (StringObject.isEmpty(val)) {
            val = request.getParameter("uuid");
        }
        return val;
    }

    public static String getWeixinOpenid(HttpServletRequest request) {
        String val = request.getHeader(HeaderKey.X_WEIXIN_OPENID);
        if (StringObject.isEmpty(val)) {
            val = request.getParameter("openid");
        }
        return val;
    }
}
