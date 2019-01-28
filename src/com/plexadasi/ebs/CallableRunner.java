/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  org.apache.commons.dbutils.AbstractQueryRunner
 *  org.apache.commons.dbutils.OutParameter
 */
package com.plexadasi.ebs;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbutils.AbstractQueryRunner;
import org.apache.commons.dbutils.OutParameter;

public class CallableRunner
extends AbstractQueryRunner {
    public CallableRunner() {
    }

    public CallableRunner(DataSource ds) {
        super(ds);
    }

    public CallableRunner(boolean pmdKnownBroken) {
        super(pmdKnownBroken);
    }

    public CallableRunner(DataSource ds, boolean pmdKnownBroken) {
        super(ds, pmdKnownBroken);
    }

    public Map<String, Object> queryProc(Connection conn, String sql, Object ... params) throws SQLException {
        return this.queryProc(conn, false, sql, params);
    }

    public Map<String, Object> queryProc(Connection conn, String sql) throws SQLException {
        return this.queryProc(conn, false, sql, (Object[])null);
    }

    public Map<String, Object> queryProc(String sql, Object ... params) throws SQLException {
        Connection conn = this.prepareConnection();
        return this.queryProc(conn, true, sql, params);
    }

    public Map<String, Object> queryProc(String sql) throws SQLException {
        Connection conn = this.prepareConnection();
        return this.queryProc(conn, true, sql, (Object[])null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Map<String, Object> queryProc(Connection conn, boolean closeConn, String sql, Object ... params) throws SQLException {
        Map<String, Object> result;
        if (conn == null) {
            throw new SQLException("Null connection");
        }
        if (sql == null) {
            if (closeConn) {
                this.close(conn);
            }
            throw new SQLException("Null SQL statement");
        }
        if (!sql.toUpperCase().contains("CALL")) {
            if (closeConn) {
                this.close(conn);
            }
            throw new SQLException("Not a callable statement");
        }
        CallableStatement stmt = null;
        result = null;
        try {
            stmt = this.prepareCall(conn, sql);
            this.fillStatement((PreparedStatement)stmt, params);
            stmt.execute();
            result = getParameter(stmt, params);
        }
        catch (SQLException e) {
            this.rethrow(e, sql, params);
        }
        finally {
            this.close((Statement)stmt);
            if (closeConn) {
                this.close(conn);
            }
        }
        return result;
    }

    @Override
    protected CallableStatement prepareCall(Connection conn, String sql) throws SQLException {
        return conn.prepareCall(sql);
    }

    private Map<String, Object> getParameter(CallableStatement cs, Object ... params) throws SQLException {
        HashMap<String, Object> list = new HashMap<String, Object>();
        for (int i = 0; i < params.length; ++i) {
            if (!(params[i] instanceof OutParameter)) continue;
            Class T = ((OutParameter)params[i]).getJavaType();
//            Object value = ((OutParameter)params[i]).getValue();
            Integer count = i + 1;
            String column = String.valueOf(count);
            if(((OutParameter)params[i]).getValue() != null)
                column = String.valueOf(((OutParameter)params[i]).getValue());
            
            switch (((OutParameter)params[i]).getSqlType()) {
                case 4: {
                    list.put(column, cs.getInt(count));
                    continue;
                }
                case 12: {
                    list.put(column, cs.getString(count));
                    continue;
                }
                case 1: {
                    list.put(column, cs.getCharacterStream(count));
                    continue;
                }
                case 6: {
                    list.put(column, cs.getFloat(count));
                    continue;
                }
                case 2005: {
                    list.put(column, cs.getClob(count));
                    continue;
                }
                default: {
                    throw new SQLException("Out parameter not implemented yet in callable statement");
                }
            }
        }
        return list;
    }
}
