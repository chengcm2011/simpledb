package com.cheng.db;

import com.cheng.lang.TimeToolkit;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 仅供DBRunner使用，请勿外部使用
 */
public class RowMapperToDbHelperMap implements RowMapper<Map<String, Object>> {

    public static final RowMapperToDbHelperMap instance = new RowMapperToDbHelperMap();

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
        String typeName = meta.getColumnTypeName(i);
        if (typeName.contains("INT"))
            return rs.getInt(i);
        else if (typeName.contains("DATETIME"))
            return TimeToolkit.getCurrentTs(rs.getTimestamp(i));
        else
            return rs.getString(i);
    }

}

