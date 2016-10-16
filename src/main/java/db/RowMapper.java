package db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {

    T apply(ResultSet rs) throws SQLException;

}
