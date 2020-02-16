package com.flash.framework.web.utils;

import com.google.common.collect.Maps;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhurg
 * @date 2019/10/14 - 上午10:44
 */
public class CookieUtils {

    public static Map<String, String> getCookies(HttpServletRequest request) {
        Map<String, String> cookieMap = Maps.newHashMap();
        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies) && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        return cookieMap;
    }
}