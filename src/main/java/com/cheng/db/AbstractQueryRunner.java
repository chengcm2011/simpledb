package com.cheng.db;

import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Arrays;

/**
 * @see org.apache.commons.dbutils.AbstractQueryRunner
 */
public abstract class AbstractQueryRunner {

    private final DataSource ds;

    public AbstractQueryRunner(DataSource ds) {
        this.ds = ds;
    }

    protected final DataSource getDataSource() {
        return this.ds;
    }

    protected PreparedStatement prepareStatement(Connection conn, String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    protected PreparedStatement prepareStatement(Connection conn, String sql, int returnedKeys) throws SQLException {
        return conn.prepareStatement(sql, returnedKeys);
    }

    protected Connection prepareConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * Fill the <code>PreparedStatement</code> replacement parameters with the
     * given objects.
     *
     * @param stmt   PreparedStatement to fill
     * @param params Query replacement parameters; <code>null</code> is a valid
     *               value to pass in.
     * @throws SQLException if a database access error occurs
     */
    public void fillStatement(PreparedStatement stmt, Object... params)
            throws SQLException {
        // check the parameter count, if we can
        ParameterMetaData pmd = stmt.getParameterMetaData();
        int stmtCount = pmd.getParameterCount();
        int paramsCount = params == null ? 0 : params.length;

        if (stmtCount != paramsCount) {
            throw new SQLException("Wrong number of parameters: expected " + stmtCount + ", was given " + paramsCount);
        }

        // nothing to do here
        if (params == null) {
            return;
        }

        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                stmt.setObject(i + 1, params[i]);
            } else {
                // VARCHAR works with many drivers regardless
                // of the actual column type. Oddly, NULL and
                // OTHER don't work with Oracle's drivers.
                int sqlType = pmd.getParameterType(i + 1);
                stmt.setNull(i + 1, sqlType);
            }
        }
    }

    /**
     * Throws a new exception with a more informative error message.
     *
     * @param cause  The original exception that will be chained to the new
     *               exception when it's rethrown.
     * @param sql    The query that was executing when the exception happened.
     * @param params The query replacement parameters; <code>null</code> is a valid
     *               value to pass in.
     * @throws SQLException if a database access error occurs
     */
    protected void rethrow(SQLException cause, String sql, Object... params)
            throws SQLException {

        String causeMessage = cause.getMessage();
        if (causeMessage == null) {
            causeMessage = "";
        }
        StringBuilder msg = new StringBuilder(causeMessage);

        msg.append(" Query: ");
        msg.append(sql);
        msg.append(" Parameters: ");

        if (params == null) {
            msg.append("[]");
        } else {
            msg.append(Arrays.deepToString(params));
        }

        SQLException e = new SQLException(msg.toString(), cause.getSQLState(),
                cause.getErrorCode());
        e.setNextException(cause);

        throw e;
    }

    /**
     * Close a <code>Connection</code>. This implementation avoids closing if
     * null and does <strong>not</strong> suppress any exceptions. Subclasses
     * can override to provide special handling like logging.
     *
     * @param conn Connection to close
     * @throws SQLException if a database access error occurs
     * @since DbUtils 1.1
     */
    protected void close(Connection conn) throws SQLException {
        DbUtils.close(conn);
    }

    /**
     * Close a <code>Statement</code>. This implementation avoids closing if
     * null and does <strong>not</strong> suppress any exceptions. Subclasses
     * can override to provide special handling like logging.
     *
     * @param stmt Statement to close
     * @throws SQLException if a database access error occurs
     * @since DbUtils 1.1
     */
    protected void close(Statement stmt) throws SQLException {
        DbUtils.close(stmt);
    }

    /**
     * Close a <code>ResultSet</code>. This implementation avoids closing if
     * null and does <strong>not</strong> suppress any exceptions. Subclasses
     * can override to provide special handling like logging.
     *
     * @param rs ResultSet to close
     * @throws SQLException if a database access error occurs
     * @since DbUtils 1.1
     */
    protected void close(ResultSet rs) throws SQLException {
        DbUtils.close(rs);
    }

}
