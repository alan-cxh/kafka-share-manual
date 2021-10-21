package com.newland.kafka.sendobjects.service.impl;

import com.newland.kafka.sendobjects.entity.Field;
import com.newland.kafka.sendobjects.entity.FieldObj;
import com.newland.kafka.sendobjects.entity.Payload;
import com.newland.kafka.sendobjects.entity.TopicEntity;
import com.newland.kafka.sendobjects.service.KafkaToDbService;
import com.newland.kafka.sendobjects.utils.*;
import org.postgresql.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class KafkaToDbServiceImpl implements KafkaToDbService {
    private final Logger logger = LoggerFactory.getLogger(KafkaToDbServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void handleKafkaMessage(Acknowledgment acknowledgment, Map<String, Object> kafkaKey, TopicEntity topicEntity) {
        try {
            Payload payload = topicEntity.getPayload();
            FieldObj fieldObj = topicEntity.getSchema().getField("before");

            Map<String, Object> beforeMap = payload.getBefore();
            Map<String, Object> afterMap = payload.getAfter();
            String op = payload.getOp();
            String table = payload.getSource().getTable();
            logger.info(JsonUtils.toJson(topicEntity));
            String sql = "";
            switch (op) {
                case "u":
                    logger.info("update operate for data");
                    sql = this.parseUpdateSql(kafkaKey, fieldObj, afterMap, table);
                    break;
                case "d":
                    logger.info("delete operate for data");
                    sql = this.parseDeleteSql(kafkaKey, beforeMap, table);
                    break;
                case "c":
                case "r":
                    logger.info("insert operate for data");
                    sql = this.parseInsertSql(fieldObj, afterMap, table);
                    break;
                default:
                    logger.warn("operator choose error, pls confirm...");
                    throw new Exception("operator choose error");
            }
            logger.info(">>> {}", sql);
            if (sql.contains("?")) {
                // 存在二进制字段，特殊处理
                this.dealWithBlob(sql, fieldObj, afterMap);
            } else {
                jdbcTemplate.execute(sql);
            }
            //提交offset
            acknowledgment.acknowledge();
            logger.info("提交offset");
        } catch (Exception e) {
            logger.error("kafka处理消息异常：", e);
        }
    }

    private void dealWithBlob(String sql, FieldObj fieldObj, Map<String, Object> afterMap) {
        List<byte[]> columnValuesList = new ArrayList<>();
        fieldObj.getFields().forEach(e -> {
            if (DebeziumFieldEnum.bytes.getValue().equals(e.getType())) {
                // org.postgresql.util.Base64 解码成byte[]
                columnValuesList.add(Base64.decode((String) afterMap.get(e.getField())));
            }
        });
        JdbcUtils.executeWithBlob(sql, columnValuesList, jdbcTemplate);
    }


    private String parseUpdateSql(Map<String, Object> kafkaKey, FieldObj fieldObj, Map<String, Object> afterData, String table) {
        for (String key : kafkaKey.keySet()) {
            afterData.remove(key);
        }
        StringBuilder builder = this.parseUpdateDll(fieldObj, kafkaKey, afterData, table);
        return builder.toString();
    }

    private StringBuilder parseUpdateDll(FieldObj fieldObj, Map<String, Object> kafkaKey, Map<String, Object> updateDataMap, String table) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" update ").append(table).append(" set ");
        for (Map.Entry<String, Object> updateData : updateDataMap.entrySet()) {
            if (updateData.getValue() == null) {
                sqlBuilder.append(updateData.getKey()).append(" = null").append(",");
            } else {
                if (!this.handleField(fieldObj, updateData.getKey(), updateData.getValue(), sqlBuilder, false)) {
                    sqlBuilder.append(updateData.getKey()).append(" = ").append("'").append(updateData.getValue()).append("'").append(",");
                }
            }
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);

        sqlBuilder.append(" where ");
        for (Map.Entry<String, Object> map : kafkaKey.entrySet()) {
            if (map.getValue() != null) {
                sqlBuilder.append(map.getKey()).append(" = ").append("'").append(map.getValue()).append("' and ");
            }
        }
        sqlBuilder.delete(sqlBuilder.length() - 5, sqlBuilder.length());
        return sqlBuilder;
    }

    /**
     * 处理特定字段类型
     */
    private boolean handleField(FieldObj fieldObj, String columnName, Object columnValue, StringBuilder sqlBuilder, boolean isCondition) {
        List<Field> beforeFieldList = fieldObj.getFields();

        for (Field field : beforeFieldList) {
            if (field.getField().equals(columnName) && field.getName() != null) {
                if (DebeziumFieldEnum.date.getValue().equals(field.getName())) {
                    ConvertUtils.convertUpdateDate(columnName, columnValue, sqlBuilder, isCondition);
                    return true;
                } else if (DebeziumFieldEnum.zoned_timestamp.getValue().equals(field.getName())) {
                    ConvertUtils.convertZonedTimestamp(columnName, columnValue, sqlBuilder, isCondition);
                    return true;
                } else if (DebeziumFieldEnum.micro_timestamp.getValue().equals(field.getName())) {
                    ConvertUtils.convertUpdateMicroTimestamp(columnName, columnValue, sqlBuilder, isCondition);
                    return true;
                }
            } else if (field.getField().equals(columnName) && field.getType().equals(DebeziumFieldEnum.bytes.getValue())) {
//                sqlBuilder.append(columnName).append(" = ").append("utl_raw.cast_to_raw('").append(columnValue).append("'),");
                sqlBuilder.append(columnName).append(" = ?,");
                return true;
            }
        }
        return false;
    }

    /**
     * 转添加Sql
     *
     * @param afterData
     * @param table
     * @return
     */
    private String parseInsertSql(FieldObj fieldObj, Map<String, Object> afterData, String table) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("insert into ").append(table).append(" ( ");

        for (String key : afterData.keySet()) {
            sqlBuilder.append(key).append(",");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1).append(" ) values (");

        for (Map.Entry<String, Object> entry : afterData.entrySet()) {
            if (entry.getValue() == null) {
                sqlBuilder.append("null").append(",");
            } else {
                sqlBuilder.append(handleInsertField(fieldObj, entry.getKey(), entry.getValue())).append(",");
            }
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1).append(" ) ");

        return sqlBuilder.toString();
    }

    private String handleInsertField(FieldObj fieldObj, String columnName, Object columnValue) {
        List<Field> beforeFieldList = fieldObj.getFields();

        StringBuilder sbf = new StringBuilder();
        for (Field field : beforeFieldList) {
            if (field.getField().equals(columnName)) {
                if (DebeziumFieldEnum.date.getValue().equalsIgnoreCase(field.getName())) {
                    sbf.append("to_date('").append(ConvertUtils.convertDate(columnValue)).append("', '")
                            .append(DateUtils.SHORT_DATE_FORMAT).append("')");
                    return sbf.toString();
                } else if (DebeziumFieldEnum.micro_timestamp.getValue().equalsIgnoreCase(field.getName())) {
                    sbf.append("to_date('").append(ConvertUtils.convertMicroTimestamp(columnValue)).append("', '")
                            .append(DateUtils.LONG_DATE_SQL_FORMAT).append("')");
                    return sbf.toString();
                } else if (DebeziumFieldEnum.zoned_timestamp.getValue().equalsIgnoreCase(field.getName())) {
                    sbf.append("to_date('").append(ConvertUtils.convertZonedTimestamp(columnValue)).append("', '")
                            .append(DateUtils.LONG_DATE_SQL_FORMAT).append("')");
                    return sbf.toString();
                } else {
                    if (field.getType().equals(DebeziumFieldEnum.bytes.getValue())) {
                        // ？问号代表占位符
                        sbf.append("?");
                        return sbf.toString();
                    }
                    sbf.append("'").append(columnValue).append("'");
                    return sbf.toString();
                }
            }
        }
        sbf.append("'").append(columnValue).append("'");
        return sbf.toString();
    }

    /**
     * 转删除sql
     *
     * @param beforeData
     * @param table
     * @return
     */
    private String parseDeleteSql(Map<String, Object> kafkaKey, Map<String, Object> beforeData, String table) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("delete from ").append(table).append(" where ");
        for (Map.Entry<String, Object> map : kafkaKey.entrySet()) {
            if (map.getValue() != null) {
                sqlBuilder.append(map.getKey()).append(" = ").append("'").append(map.getValue()).append("' and ");
            }
        }
        sqlBuilder.delete(sqlBuilder.length() - 5, sqlBuilder.length());
        return sqlBuilder.toString();
    }
}
