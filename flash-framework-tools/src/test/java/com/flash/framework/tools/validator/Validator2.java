package com.flash.framework.tools.validator;

import com.flash.framework.tools.validators.Validator;

/**
 * @author zhurg
 * @date 2018/11/25 - 下午8:36
 */
public class Validator2 extends Validator<String> {

    public Validator2(){
        setSort(2);
    }

    @Override
    public boolean validate(String params) {
        return "validator".equals(params);
    }

    @Override
    public String errorMsg() {
        return "validator2 error";
    }
}
