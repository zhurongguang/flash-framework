package com.flash.framework.core.support.sequence.generator;

import com.flash.framework.commons.utils.JacksonUtils;
import com.flash.framework.core.support.sequence.Sequence;
import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @author zhurg
 * @date 2021/6/25 - 下午1:53
 */
@Component
@ConditionalOnBean(Sequence.class)
public class DefaultIdGenerator implements IdGenerator {

    @Autowired
    private Sequence sequence;

    @Override
    public String generate(Object obj) {
        String sequenceName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, obj.getClass().getSimpleName());
        IDRule idRule = AnnotationUtils.findAnnotation(obj.getClass(), IDRule.class);
        StringBuffer seq = new StringBuffer();
        if (Objects.nonNull(idRule) && StringUtils.isNotBlank(idRule.prefix())) {
            String[] params = idRule.prefix().split("\\+");
            if (Objects.nonNull(params) && params.length > 0) {
                Map<String, Object> fields = JacksonUtils.nonEmptyMapper().fromJson(JacksonUtils.nonEmptyMapper().toJson(obj), Map.class);
                for (String param : params) {
                    if (param.startsWith("#")) {
                        param = param.replaceFirst("#", "");
                        seq.append(fields.getOrDefault(param, "").toString());
                    } else {
                        seq.append(param);
                    }
                }
            }
        }
        seq.append(sequence.nextValue(sequenceName, idRule));
        return seq.toString();
    }
}