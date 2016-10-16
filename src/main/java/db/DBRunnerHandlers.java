package db;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.AbstractListHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class DBRunnerHandlers {

	/* ------------------------- single ------------------------- */

    public static final ResultSetHandler<Integer> singleIntHandler = new ResultSetHandler<Integer>() {
        public Integer handle(ResultSet rs) throws SQLException {
            return rs.next() ? rs.getInt(1) : 0;
        }
    };
    public static final ResultSetHandler<Long> singleLongHandler = new ResultSetHandler<Long>() {
        public Long handle(ResultSet rs) throws SQLException {
            return rs.next() ? rs.getLong(1) : 0;
        }
    };
    public static final ResultSetHandler<Double> singleDoubleHandler = new ResultSetHandler<Double>() {
        public Double handle(ResultSet rs) throws SQLException {
            return rs.next() ? rs.getDouble(1) : 0;
        }
    };
    public static final ResultSetHandler<String> singleStringHandler = new ResultSetHandler<String>() {
        public String handle(ResultSet rs) throws SQLException {
            return rs.next() ? rs.getString(1) : null;
        }
    };
    public static final ResultSetHandler<Timestamp> singleTimestampHandler = new ResultSetHandler<Timestamp>() {
        public Timestamp handle(ResultSet rs) throws SQLException {
            return rs.next() ? rs.getTimestamp(1) : null;
        }
    };

	/* ------------------------- list ------------------------- */

    public static final ResultSetHandler<List<Integer>> intListHandler = new AbstractListHandler<Integer>() {
        protected Integer handleRow(ResultSet rs) throws SQLException {
            return rs.getInt(1);
        }
    };
    public static final ResultSetHandler<List<Long>> longListHandler = new AbstractListHandler<Long>() {
        protected Long handleRow(ResultSet rs) throws SQLException {
            return rs.getLong(1);
        }
    };
    public static final ResultSetHandler<List<String>> stringListHandler = new AbstractListHandler<String>() {
        protected String handleRow(ResultSet rs) throws SQLException {
            return rs.getString(1);
        }
    };


}
