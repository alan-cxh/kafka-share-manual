package com.newland.kafka.sendobjects.utils;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Administrator
 */
public class JdbcUtils {

    /**
     * 更新oracle数据的BLOB二进制字段
     * @param sql
     * @param columnValueList
     * @param jdbcTemplate
     */
    public static void executeWithBlob(String sql, List<byte[]> columnValueList, JdbcTemplate jdbcTemplate) {
        LobHandler lobHandler = new DefaultLobHandler();
        jdbcTemplate.execute(sql, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
            @Override
            protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException, DataAccessException {
                for (int i = 0; i < columnValueList.size(); i++) {
                    lobCreator.setBlobAsBytes(ps, i+1, columnValueList.get(i));
                }
            }
        });
    }
}
