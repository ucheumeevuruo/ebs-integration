/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.objects.Impl;

/**
 *
 * @author SAP Training
 */
import com.plexadasi.connect.build.MyLogging;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.apache.commons.dbutils.AbstractQueryRunner;
import org.apache.commons.dbutils.OutParameter;
import org.apache.commons.dbutils.ResultSetHandler;

/**
 * Based on the DbUtil hierarchy, this class allows us to call stored
 * procedures from DbUtil. Executes SQL stored procedure calls with pluggable
 * strategies for handling ResultSets. This class is thread safe.
 *
 * @author javabob64
 *
 * @see ResultSetHandler
 */
public class CallableRunner extends AbstractQueryRunner {

    public CallableRunner() {
	super();
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

    public Map<Integer, Object> queryProc(Connection conn, String sql,
                           Object... params)
	         throws SQLException {
	return this.queryProc(conn, false, sql, params);
    }

    public Map<Integer, Object> queryProc(Connection conn, String sql) throws SQLException {
	 return this.queryProc(conn, false, sql, (Object[]) null);
    }

    public Map<Integer, Object> queryProc(String sql,
                           Object... params) throws SQLException {
	Connection conn = this.prepareConnection();
        return this.queryProc(conn, true, sql, params);
    }

    public Map<Integer, Object> queryProc(String sql)
                 throws SQLException {
        Connection conn = this.prepareConnection();
        return this.queryProc(conn, true, sql, (Object[]) null);
    }

    /**
     * Calls stored procedure after checking the parameters to ensure nothing
     * is null.
     * @param conn The connection to use for the query call.
     * @param closeConn True if the connection should be closed, else false.
     * @param sql The stored procedure call to execute.
     * @param params An array of query replacement parameters.  Each row in
     * this array is one set of batch replacement values.
     * @return The results of the query.
     * @throws SQLException If there are database or parameter errors.
     */
    private Map<Integer, Object> queryProc(Connection conn, boolean closeConn, String sql, Object... params)
                            throws SQLException {
	if (conn == null) {
		throw new SQLException("Null connection");
	}
	if (sql == null) {
		if (closeConn) {close(conn);}
		throw new SQLException("Null SQL statement");
	}
	if (sql.toUpperCase().indexOf("CALL") == -1) {
		if (closeConn) {close(conn);}
		throw new SQLException("Not a callable statement");
	}
	CallableStatement stmt = null;
        Map<Integer, Object> result = null;

	try {
            stmt = this.prepareCall(conn, sql);
            this.fillStatement(stmt, params);
            stmt.execute();
            result = this.getParameter(stmt, params);
	} catch (SQLException e) {
		this.rethrow(e, sql, params);
	} finally {
            close(stmt);
            if (closeConn) {
                    close(conn);
            }
	}
	return result;
    }

    @Override
    protected CallableStatement prepareCall(Connection conn, String sql)
                                throws SQLException {
    	return conn.prepareCall(sql);
    }
    
    private Map<Integer, Object> getParameter(CallableStatement cs, Object... params) throws SQLException{
        Map<Integer, Object> list = new HashMap();
        for (int i = 0; i < params.length; i++) {
            if(params[i] instanceof OutParameter){
                Class T = ((OutParameter)params[i]).getJavaType();
                Integer col = i + 1;
                
                switch(((OutParameter)params[i]).getSqlType()){
                    case java.sql.Types.INTEGER:
                        list.put(col, cs.getInt(col));
                    break;
                    case java.sql.Types.VARCHAR:
                        list.put(col, cs.getString(col));
                    break;
                    case java.sql.Types.CHAR:
                        list.put(col, cs.getCharacterStream(col));
                    break;
                    case java.sql.Types.FLOAT:
                        list.put(col, cs.getFloat(col));
                    break;
                    case java.sql.Types.CLOB:
                        list.put(col, cs.getClob(col));
                    break;
                    default:
                        throw new SQLException("Out parameter not implemented"
                                + " yet in callable statement");
                }
            }
        }
        return list;
    }
}//end of class ProcRunner