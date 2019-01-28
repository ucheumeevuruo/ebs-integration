/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  org.apache.commons.dbutils.AbstractQueryRunner
 *  org.apache.commons.dbutils.ResultSetHandler
 */
package com.plexadasi.ebs;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.apache.commons.dbutils.AbstractQueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

public class ProcRunner
extends AbstractQueryRunner {
    public ProcRunner() {
    }

    public ProcRunner(DataSource ds) {
        super(ds);
    }

    public ProcRunner(boolean pmdKnownBroken) {
        super(pmdKnownBroken);
    }

    public ProcRunner(DataSource ds, boolean pmdKnownBroken) {
        super(ds, pmdKnownBroken);
    }

    public /* varargs */ <T> T queryProc(Connection conn, String sql, ResultSetHandler<T> rsh, Object ... params) throws SQLException {
        return this.queryProc(conn, false, sql, rsh, params);
    }

    public <T> T queryProc(Connection conn, String sql, ResultSetHandler<T> rsh) throws SQLException {
        return this.queryProc(conn, false, sql, rsh, (Object[])null);
    }

    public /* varargs */ <T> T queryProc(String sql, ResultSetHandler<T> rsh, Object ... params) throws SQLException {
        Connection conn = this.prepareConnection();
        return this.queryProc(conn, true, sql, rsh, params);
    }

    public <T> T queryProc(String sql, ResultSetHandler<T> rsh) throws SQLException {
        Connection conn = this.prepareConnection();
        return this.queryProc(conn, true, sql, rsh, (Object[])null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private /* varargs */ <T> T queryProc(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> rsh, Object ... params) throws SQLException {
        Object result;
        if (conn == null) {
            throw new SQLException("Null connection");
        }
        if (sql == null) {
            if (closeConn) {
                this.close(conn);
            }
            throw new SQLException("Null SQL statement");
        }
        if (rsh == null) {
            if (closeConn) {
                this.close(conn);
            }
            throw new SQLException("Null ResultSetHandler");
        }
        if (sql.toUpperCase().indexOf("CALL") == -1) {
            if (closeConn) {
                this.close(conn);
            }
            throw new SQLException("Not a callable statement");
        }
        CallableStatement stmt = null;
        ResultSet rs = null;
        result = null;
        try {
            stmt = this.prepareCall(conn, sql);
            this.fillStatement((PreparedStatement)stmt, params);
            rs = this.wrap(stmt.executeQuery());
            result = rsh.handle(rs);
        }
        catch (SQLException e) {
            this.rethrow(e, sql, params);
        }
        finally {
            try {
                this.close(rs);
            }
            finally {
                this.close((Statement)stmt);
                if (closeConn) {
                    this.close(conn);
                }
            }
        }
        return (T)result;
    }

    protected CallableStatement prepareCall(Connection conn, String sql) throws SQLException {
        return conn.prepareCall(sql);
    }
}

