package com.cheng.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 仅供DBRunner使用，请勿外部使用
 */
public class RowMapperToRawMap implements RowMapper<Map<String, Object>> {

    public static final RowMapperToRawMap instance = new RowMapperToRawMap();

    @Override
    public Map<String, Object> apply(ResultSet rs) throws SQLException {
        Map<String, Object> map = new HashMap<>();
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();
        for (int i = 1; i <= cols; i++) {
            String key = meta.getColumnLabel(i).toLowerCase();
            Object val = getVal(rs, meta, i);
            map.put(key, val);
        }
        return map;
    }

    private Object getVal(ResultSet rs, ResultSetMetaData meta, int i) throws SQLException {
        return rs.getObject(i);
    }

}

