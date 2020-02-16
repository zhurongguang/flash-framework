package com.flash.framework.tools.validator;

import com.flash.framework.tools.validators.Validator;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhurg
 * @date 2018/11/25 - 下午8:34
 */
public class Validator1 extends Validator<String> {

    public Validator1() {
        setSort(1);
    }

    @Override
    public boolean validate(String params) {
        return StringUtils.isNotBlank(params);
    }

    @Override
    public String errorMsg() {
        return "validator1 error";
    }
}
