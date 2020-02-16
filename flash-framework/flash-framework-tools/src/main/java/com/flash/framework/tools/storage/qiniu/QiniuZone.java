package com.flash.framework.tools.storage.qiniu;

import com.qiniu.common.Zone;
import lombok.Getter;

/**
 * @author zhurg
 * @date 2019/6/13 - 上午11:15
 */
public enum QiniuZone {

    zone0(Zone.zone0()),
    huadong(Zone.huadong()),
    qvmZone0(Zone.qvmZone0()),
    qvmHuadong(Zone.qvmHuadong()),
    zone1(Zone.zone1()),
    huabei(Zone.huabei()),
    qvmZone1(Zone.qvmZone1()),
    qvmHuabei(Zone.qvmHuabei()),
    zone2(Zone.zone2()),
    huanan(Zone.huanan()),
    zoneNa0(Zone.zoneNa0()),
    beimei(Zone.beimei()),
    zoneAs0(Zone.zoneAs0()),
    xinjiapo(Zone.xinjiapo())
    ;

    @Getter
    private Zone zone;

    QiniuZone(Zone zone) {
        this.zone = zone;
    }
}