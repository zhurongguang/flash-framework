package com.flash.framework.tools.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.flash.framework.tools.validators.ValidatorChain;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * EasyExcel AnalysisEventListener 抽象实现
 *
 * @author zhurg
 * @date 2018/11/25 - 上午11:07
 */
public abstract class AbstractExcelListener<T> extends AnalysisEventListener<T> {

    @Getter
    @Setter
    private ValidatorChain<T> validators;

    @Getter
    @Setter
    protected List<Object> datas = Lists.newArrayList();

    @Getter
    @Setter
    protected List<ReadExcelError> errors = Lists.newArrayList();

    private int line = 0;

    @Override
    public void invoke(T o, AnalysisContext analysisContext) {
        if (null != validators && !validators.isEmpty()) {
            try {
                validators.doValidatorChain(o);
                datas.add(o);
            } catch (Exception e) {
                ReadExcelError error = ReadExcelError.builder()
                        .message(e.getMessage())
                        .line(line + 1)
                        .data(o)
                        .build();
                errors.add(error);
            }
        } else {
            datas.add(o);
        }
        line++;
    }

    /**
     * 读完数据后一定要先调用resetLine()，重置行数，否则会影响下一次读取
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        resetLine();
    }

    /**
     * 重置行数
     */
    public void resetLine() {
        this.line = 0;
    }

    /**
     * 是否存在失败记录
     *
     * @return
     */
    public boolean hasError() {
        return CollectionUtils.isNotEmpty(errors);
    }
}