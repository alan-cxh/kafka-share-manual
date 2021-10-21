package com.alan.kafkaexample.consume.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 告警信息表
 * @author Administrator
 */
@Data
@ToString
@Accessors(chain = true)
public class TFAlarmMessage implements Serializable {
    private Long alarmMessageId;

    @ApiModelProperty(value = "采集时间")
    private Date alcolTime;

    @ApiModelProperty(value = "告警发生次数")
    private String alarmCount;

    @ApiModelProperty(value = "网管告警级别id 枚举值:(1，2，3，4)")
    private String stdSeverityId;

    @ApiModelProperty(value = "网管告警级别 枚举值:(一级告警， 二级告警， 三级告警， 四级告警)")
    private String stdSeverity;

    @ApiModelProperty(value = "告警状态id 枚举值:(0,1)")
    private String alarmStatusId;

    @ApiModelProperty(value = "告警状态 枚举值:(0:已清除 1:未清除)")
    private String alarmStatus;

    @ApiModelProperty(value = "关联标识(是否关联告警) 枚举值:(已关联，未关联)")
    private String relationFlag;

    @ApiModelProperty(value = "系统名称 枚举值:(NB监控 室分监控 智控云 能源监控 视频监控平台  资产运营监控 运维监控)")
    private String omcName;

    @ApiModelProperty(value = "活动告警入库时间")
    private Date insertTime;

    @ApiModelProperty(value = "告警确认状态id 枚举值:(0:未确认 1:已确认)")
    private String ackStatusId;

    @ApiModelProperty(value = "告警确认状态 枚举值:(已确认，未确认)")
    private String ackStatus;

    @ApiModelProperty(value = "告警确认人")
    private String ackUser;

    @ApiModelProperty(value = "告警确认时间")
    private Date ackTime;

    @ApiModelProperty(value = "告警正文")
    private String alarmText;

    @ApiModelProperty(value = "地市名称")
    private String cityName;

    @ApiModelProperty(value = "告警私有ID")
    private String subuuid;

    @ApiModelProperty(value = "告警发生时间")
    private Date occurTime;

    @ApiModelProperty(value = "告警清除时间")
    private Date clearTime;

    @ApiModelProperty(value = "派单状态ID 枚举值:(0，1，2，3，4，5，6）")
    private String dispatchStateId;

    @ApiModelProperty(value = "派单状态名称  枚举值:(0:未派单 1:等待派单 2:人工终止派单 3:系统抑制派单  4:派单失败  5:自动派单成功 6:手工派单成功")
    private String dispatchState;

    @ApiModelProperty(value = "告警备注")
    private String alarmNote;

    @ApiModelProperty(value = "告警标题")
    private String alarmTitle;

    @ApiModelProperty(value = "告警信号量id")
    private String signalId;

    @ApiModelProperty(value = "省份名称")
    private String provinceName;

    @ApiModelProperty(value = "区县名称")
    private String countyName;

    @ApiModelProperty(value = "设备id")
    private String neUid;

    @ApiModelProperty(value = "ne_name")
    private String neTypeId;

    @ApiModelProperty(value = "设备细分类型")
    private String neType;

    @ApiModelProperty(value = "设备状态")
    private String deviceStatus;

    @ApiModelProperty(value = "设备资源id")
    private String deviceResourceId;

    @ApiModelProperty(value = "设备资源类型")
    private String deviceResourceType;

    @ApiModelProperty(value = "设备厂家名称")
    private String vendorName;

    @ApiModelProperty(value = "站址类型")
    private String siteType;

    @ApiModelProperty(value = "告警对象id")
    private String locateNeUid;

    @ApiModelProperty(value = "告警对象资源ID")
    private String objectResourceId;

    @ApiModelProperty(value = "告警对象资源类型")
    private String objectResourceType;

    @ApiModelProperty(value = "告警对象细分类型id")
    private String locateNeClassId;

    @ApiModelProperty(value = "告警对象细分类型")
    private String locateNeClass;

    @ApiModelProperty(value = "移动维护服务等级")
    private String chinamobileLevel;

    @ApiModelProperty(value = "联通维护服务等级")
    private String chinaunicomLevel;

    @ApiModelProperty(value = "电信维护服务等级")
    private String chinatelecomLevel;

    @ApiModelProperty(value = "省份id")
    private String provinceId;

    @ApiModelProperty(value = "地市id")
    private String cityId;

    @ApiModelProperty(value = "区县id")
    private String countyId;

    @ApiModelProperty(value = "代维厂家")
    private String serviceVendor;

    @ApiModelProperty(value = "厂家id")
    private String vendorId;

    @ApiModelProperty(value = "站址id")
    private String siteCode;

    @ApiModelProperty(value = "站址名称")
    private String siteName;

    @ApiModelProperty(value = "机房编码")
    private String roomUid;

    @ApiModelProperty(value = "告警对象名称")
    private String locateNeName;

    @ApiModelProperty(value = "运营商名称")
    private String operatorName;

    @ApiModelProperty(value = "site_resource")
    private String siteResource;

    @ApiModelProperty(value = "所属业务")
    private String business;

    @ApiModelProperty(value = "所属子业务")
    private String subBusiness;

    @ApiModelProperty(value = "告警流水号")
    private String serialno;


}