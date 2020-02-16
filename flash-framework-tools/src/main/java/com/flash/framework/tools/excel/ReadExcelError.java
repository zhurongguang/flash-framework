package com.flash.framework.tools.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhurg
 * @date 2018/11/25 - 上午11:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadExcelError implements Serializable {


    private static final long serialVersionUID = -1262028346149948142L;

    private int line;

    private Object data;

    private String message;
}