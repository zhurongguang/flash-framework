package com.flash.framework.tools.validator;

import com.flash.framework.tools.validators.ValidatorChain;
import com.google.common.collect.Lists;

/**
 * @author zhurg
 * @date 2018/11/25 - 下午8:38
 */
public class TestValidator {

    public static void main(String[] args) {
        Validator1 validator1 = new Validator1();
        Validator2 validator2 = new Validator2();
        ValidatorChain<String> validatorChain = new ValidatorChain<>(Lists.newArrayList(validator1,validator2));

        try {
            System.out.println(validatorChain.doValidatorChain(""));
            System.out.println(validatorChain.doValidatorChain("11"));
            System.out.println(validatorChain.doValidatorChain("validator"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}