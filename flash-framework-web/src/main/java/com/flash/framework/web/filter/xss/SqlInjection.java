package com.flash.framework.web.filter.xss;

/**
 * SQL注入攻击
 * <p>
 */
public class SqlInjection {

    /**
     * @Description SQL注入内容剥离
     * @param value
     *              待处理内容
     * @return
     */
    public String strip(String value) {

        //剥离SQL注入部分代码
        return value.replaceAll("('.+--)|(--)|(\\|)|(%7C)", "");
    }
}