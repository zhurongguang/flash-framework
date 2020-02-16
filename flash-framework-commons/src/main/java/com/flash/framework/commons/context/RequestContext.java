package com.flash.framework.commons.context;

/**
 * 请求上下文获取接口，可以获取当前登录用户信息、租户信息、请求ip等
 *
 * @author zhurg
 * @date 2019/6/18 - 下午1:45
 */
public interface RequestContext {

    /**
     * 获取当前用户ID
     *
     * @return
     */
    Long getUserId();

    /**
     * 获取当前用户租户ID
     *
     * @return
     */
    Integer getTenantId();

    /**
     * 获取当前登录用户名
     *
     * @return
     */
    String getUserName();

    /**
     * 获取当前登录用户IP
     *
     * @return
     */
    String getIp();
}