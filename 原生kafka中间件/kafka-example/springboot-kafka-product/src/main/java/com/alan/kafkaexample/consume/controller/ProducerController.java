package com.alan.kafkaexample.consume.controller;

import cn.hutool.core.date.DateUtil;
import com.alan.kafkaexample.consume.entity.User;
import com.alan.kafkaexample.consume.service.KafkaProducer;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("product")
public class ProducerController {

    @Autowired(required = false)
    private KafkaProducer kafkaProducer;


    @RequestMapping("/send")
    public String send(@RequestParam String msg) throws ExecutionException, InterruptedException {
        User user;
        user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("1");
        user.setHomeAddr(msg);
        user.setBirthday(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        kafkaProducer.send(user);

        return JSON.toJSONString(user);
    }

    @PostMapping("/send2")
    public String send2(@RequestBody Map<String, Object> params) {
        kafkaProducer.send1("111", JSON.toJSONString(params));
        return "test";
    }


    @PostMapping("message/send1")
    public String send1(@RequestParam String msg) {
        String jsonContent = "{\n" +
                "\"alcol_time\":\"2021-02-24 11:41:26\",\n" +
                "\"alarm_count\":\"1\",\n" +
                "\"dispatch_state_id\":\"0\",\n" +
                "\"site_type\":\"地面站\",\n" +
                "\"object_resource_id\":\"3458000000005201371\",\n" +
                "\"std_logic_subclass\":\"\",\n" +
                "\"specialty1_id\":\"5\",\n" +
                "\"uuid\":\"BB7882C6F1B01BB2E0530B801DAC6E4F\",\n" +
                "\"chinamobile_level\":\"\",\n" +
                "\"std_severity_id\":\"2\",\n" +
                "\"county_id\":\"130928\",\n" +
                "\"object_resource_type\":\"红外\",\n" +
                "\"alarm_status\":\"已清除\",\n" +
                "\"std_logic_class\":\"\",\n" +
                "\"locate_ne_class_id\":\"\",\n" +
                "\"ack_status_id\":\"0\",\n" +
                "\"locate_ne_uid\":\"13092841819001\",\n" +
                "\"engineering_status\":\"\",\n" +
                "\"ack_time\":\"\",\n" +
                "\"device_status\":\"\",\n" +
                "\"ack_user\":\"\",\n" +
                "\"sub_business\":\"智控\",\n" +
                "\"ne_name\":\"吴桥广电局/FSU01\",\n" +
                "\"alarm_text\":\"\",\n" +
                "\"device_resource_type\":\"动力及环境监控单元\",\n" +
                "\"vendor_name\":\"江苏亚奥\",\n" +
                "\"message_type\":\"10000\",\n" +
                "\"alarm_status_id\":\"0\",\n" +
                "\"std_effect_on_equ\":\"0\",\n" +
                "\"clear_time\":\"\",\n" +
                "\"province_name\":\"河北省\",\n" +
                "\"engineering_end_time\":\"\",\n" +
                "\"std_effect_on_equ_id\":\"0\",\n" +
                "\"site_name\":\"吴桥广电局\",\n" +
                "\"locate_ne_class\":\"\",\n" +
                "\"device_resource_id\":\"353600000000527095\",\n" +
                "\"vendor_id\":\"10009\",\n" +
                "\"ne_uid\":\"13092843809001\",\n" +
                "\"std_severity\":\"二级告警\",\n" +
                "\"sub_message_type\":\"11000\",\n" +
                "\"std_status\":\"0\",\n" +
                "\"std_rule_id\":\"无\",\n" +
                "\"chinatelecom_level\":\"\",\n" +
                "\"city_id\":\"130900\",\n" +
                "\"relation_flag\":\"未关联\",\n" +
                "\"std_effect_on_bus_id\":\"0\",\n" +
                "\"omc_name\":\"智控云\",\n" +
                "\"ne_version\":\"\",\n" +
                "\"std_alarm_type\":\"0\",\n" +
                "\"service_vendor\":\"\",\n" +
                "\"insert_time\":\"2021-02-24 11:41:26\",\n" +
                "\"site_code\":\"130928800010001675\",\n" +
                "\"ack_status\":\"未确认\",\n" +
                "\"serialno\":\"611835052\",\n" +
                "\"city_name\":\"沧州市\",\n" +
                "\"subuuid\":\"BB7882C6F1B01BB2E0530B801DAC6E4F11\",\n" +
                "\"engineering_info_no\":\"\",\n" +
                "\"occur_time\":\"2021-02-24 11:41:26\",\n" +
                "\"dispatch_state\":\"未派单\",\n" +
                "\"alarm_note\":\"\",\n" +
                "\"room_uid\":\"\",\n" +
                "\"locate_ne_name\":\"吴桥广电局/红外01\",\n" +
                "\"std_effect_on_bus\":\"0\",\n" +
                "\"site_resource\":\"社会站\",\n" +
                "\"business\":\"智联业务\",\n" +
                "\"alarm_title\":\"红外告警\",\n" +
                "\"probable_cause_text\":\"\",\n" +
                "\"signal_id\":\"0418003001\",\n" +
                "\"county_name\":\"吴桥县\",\n" +
                "\"operator_name\":\"\",\n" +
                "\"std_alarm_title\":\"\",\n" +
                "\"province_id\":\"130000\",\n" +
                "\"specialty1\":\"智控云\",\n" +
                "\"ne_type\":\"FSU\",\n" +
                "\"chinaunicom_level\":\"\",\n" +
                "\"ne_type_id\":\"35360001\",\n" +
                "\"engineering_start_time\":\"\"\n" +
                "}";
        kafkaProducer.send1("111", msg);
        return msg;
    }


}
