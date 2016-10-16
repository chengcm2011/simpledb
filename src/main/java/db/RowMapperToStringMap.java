package db;


import com.cheng.lang.TimeToolkit;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 仅供DBRunner使用，请勿外部使用
 */
public class RowMapperToStringMap implements RowMapper<Map<String, String>> {

    public static final RowMapperToStringMap instance = new RowMapperToStringMap();

    @Override
    public Map<String, String> apply(ResultSet rs) throws SQLException {
        Map<String, String> map = new HashMap<>();
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();
        for (int i = 1; i <= cols; i++) {
            String key = meta.getColumnLabel(i).toLowerCase();
            String val = getVal(rs, meta, i);
            map.put(key, val);
        }
        return map;
    }

    private String getVal(ResultSet rs, ResultSetMetaData meta, int i) throws SQLException {
        String typeName = meta.getColumnTypeName(i);
        if (typeName.contains("DATETIME"))
            return TimeToolkit.getCurrentTs(rs.getTimestamp(i));
        else
            return rs.getString(i);
    }

}

