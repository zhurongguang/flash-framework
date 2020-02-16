package com.flash.framework.web;

import com.flash.framework.web.filter.xss.XSSFilter;
import com.flash.framework.web.support.version.WebMvcRegistrationsExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * @author zhurg
 * @date 2019/2/2 - 上午11:20
 */
@Configuration
@ComponentScan({"com.flash.framework.web"})
public class WebConfiguration {

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return slr;
    }

    @Bean
    public WebMvcRegistrationsExtension webMvcRegistrationsExtension() {
        return new WebMvcRegistrationsExtension();
    }

    @Bean
    public XSSFilter xssFilter() {
        return new XSSFilter();
    }

    @Bean
    public FilterRegistrationBean xssFilterRegistration(@Value("${flash.filter.xss:-1000}") int order) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(xssFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(order);
        registration.setName("xssFilter");
        return registration;
    }
}