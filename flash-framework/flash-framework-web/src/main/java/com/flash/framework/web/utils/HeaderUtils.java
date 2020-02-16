package com.flash.framework.web.utils;

import com.google.common.collect.Maps;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author zhurg
 * @date 2019/10/14 - 上午10:49
 */
public class HeaderUtils {

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";

    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headerMap = Maps.newHashMap();
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(forwardedFor)) {
            headerMap.put("X_REMOTE_IP", request.getRemoteHost());
        } else {
            String[] ips = forwardedFor.split(", ");
            headerMap.put("X_REMOTE_IP", ips[0]);
        }

        Enumeration names = request.getHeaderNames();

        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = request.getHeader(name);
            headerMap.put(name, value);
        }

        return headerMap;
    }
}