package com.flash.framework.web.filter.xss;


import com.flash.framework.web.filter.HttpFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhurg
 */
public class XSSFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        /**
         * 过滤 XSS SQL 注入
         */
        WafRequestWrapper wr = new WafRequestWrapper(request);
        filterChain.doFilter(wr, response);
    }

}
