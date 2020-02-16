package com.flash.framework.tools.storage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhurg
 * @date 2019/6/12 - 下午3:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObjectStorageResponse implements Serializable {

    private static final long serialVersionUID = 6685735764510221756L;

    private String objectName;

    private String objectUrl;
}