package com.newland.kafka.sendobjects.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * @author Administrator
 */
public class ConvertUtils {


    /**
     * Update操作，io.debezium.time.Date 类型转换
     * <p>
     * 根据debezium的Date类型返回数据规则：当前时间 减去 ‘1970-1-1’ = 天数
     *
     * @param columnName
     * @param columnValue
     * @param sqlBuilder
     * @param isCondition
     */
    public static void convertUpdateDate(String columnName, Object columnValue, StringBuilder sqlBuilder, boolean isCondition) {
        if (isCondition) {
            sqlBuilder.append(" and ").append(columnName).append(" = ")
                    .append("to_date('").append(convertDate(columnValue))
                    .append("', '").append(DateUtils.LONG_DATE_SQL_FORMAT).append("') ");
        } else {
            sqlBuilder.append(columnName).append(" = ");
            sqlBuilder.append("to_date('").append(convertDate(columnValue))
                    .append("', '").append(DateUtils.LONG_DATE_SQL_FORMAT).append("'), ");
        }
    }

    /**
     * Update操作，转 MicroTimestamp
     *
     * @param columnName
     * @param columnValue
     * @param sqlBuilder
     * @param isCondition
     */
    public static void convertUpdateMicroTimestamp(String columnName, Object columnValue, StringBuilder sqlBuilder, boolean isCondition) {
        if (columnValue == null) {
            return;
        }
        if (isCondition) {
            sqlBuilder.append(" and ").append(columnName).append(" = ")
                    .append("to_date('").append(convertMicroTimestamp(columnValue))
                    .append("', '").append(DateUtils.LONG_DATE_SQL_FORMAT).append("')");
        } else {
            sqlBuilder.append(columnName).append(" = ");
            sqlBuilder.append("to_date('").append(convertMicroTimestamp(columnValue))
                    .append("', '").append(DateUtils.LONG_DATE_SQL_FORMAT).append("'),");
        }
    }


    /**
     * @param columnName
     * @param columnValue
     * @param sqlBuilder
     * @param isCondition
     */
    public static void convertZonedTimestamp(String columnName, Object columnValue, StringBuilder sqlBuilder, boolean isCondition) {
        String format = ConvertUtils.convertZonedTimestamp(columnValue);
        if (isCondition) {
            sqlBuilder.append(" and ").append(columnName).append(" = ")
                    .append("to_date('").append(format).append("', '").append(DateUtils.LONG_DATE_SQL_FORMAT).append("')");
        } else {
            sqlBuilder.append(columnName).append(" = ")
                    .append("to_date('").append(format).append("', '").append(DateUtils.LONG_DATE_SQL_FORMAT).append("')");
        }
    }

    public static String convertDate(Object columnValue) {
        Calendar date = Calendar.getInstance();
        date.set(1970, 0, 1);
        date.add(Calendar.DATE, (Integer) columnValue);

        return DateUtils.getStringDate(date.getTime(), DateUtils.SHORT_DATE_FORMAT);
    }


    public static String convertMicroTimestamp(Object columnValue) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis((Long) columnValue / 1000);
        return DateUtils.getStringDate(date.getTime(), DateUtils.LONG_DATE_FORMAT);
    }

    public static String convertZonedTimestamp(Object columnValue) {
        String d = String.valueOf(columnValue);
        if (d.contains("Z")) {
            d = d.replace("Z", "");
        }
        LocalDateTime  parsedDate = LocalDateTime.parse(d, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.LONG_DATE_FORMAT);
        return parsedDate.format(formatter);
    }
}
