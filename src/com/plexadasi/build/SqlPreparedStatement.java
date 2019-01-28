/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.build;

import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SqlPreparedStatement {
    static StringWriter errors = new StringWriter();
    private static Connection CONN;
    private static Statement cs;
    private static PreparedStatement preparedStatement;
    protected String sqlQuery = "";
    protected static ResultSet rs;
    protected String setString = "";
    protected int length = 0;
    protected boolean returnBoolean = false;
    protected int returnInteger = 0;
    protected static final String UPDATE_CLAUSE = "UPDATE ";
    protected static final String SELECT_CLAUSE = "SELECT ";
    protected static final String INSERT_CLAUSE = "INSERT ";
    protected static final String FROM_CLAUSE = " FROM ";
    protected static final String SET_CLAUSE = "SET ";
    protected static final String WHERE_CLAUSE = "WHERE ";
    protected static final String AND_CLAUSE = "AND ";
    protected static final String OR_CLAUSE = "OR ";
    protected static final String POSITIONAL_PARAMETER = "?";
    protected static final String GROUP_BY_CLAUSE = "GROUP BY ";
    protected static final String NEW_LINE = "\n";
    protected static final String EQUALS = "=";
    protected static final String BLANK = " ";
    protected static final String COMMA_SEPERATOR = ",";
    protected static final String IS_NULL = "IS NULL";
    private static final String PARAM_EXCEPTION = "Sql Parameter for insert statement cannot be empty.";

    public SqlPreparedStatement() {
    }

    public SqlPreparedStatement(Connection ebsConn) {
        CONN = ebsConn;
    }

    public SqlPreparedStatement preparedStatement() throws SiebelBusinessServiceException {
        try {
            preparedStatement = CONN.prepareStatement(this.sqlQuery);
           // MyLogging.log(Level.INFO, this.sqlQuery);
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception at prepared statement:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        finally {
            this.sqlQuery = "";
        }
        return this;
    }

    public void setInt(int index, int value) throws SiebelBusinessServiceException {
        try {
            preparedStatement.setInt(index, value);
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception when setting number:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }

    public void setFloat(int index, Float value) throws SiebelBusinessServiceException {
        try {
            preparedStatement.setFloat(index, value.floatValue());
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception when setting string:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }

    public void setString(int index, String value) throws SiebelBusinessServiceException {
        try {
            preparedStatement.setString(index, value);
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception when setting string:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }

    public void setArray(int index, Array value) throws SiebelBusinessServiceException {
        try {
            preparedStatement.setArray(index, value);
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception when setting array:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }

    public Boolean executeUpdate() throws SiebelBusinessServiceException {
        this.returnInteger = 0;
        try {
            this.returnInteger = preparedStatement.executeUpdate();
            if (this.returnInteger > 0) {
                CONN.commit();
                this.returnBoolean = true;
            } else {
                CONN.rollback();
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception at execute update method:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return this.returnBoolean;
    }

    public SqlPreparedStatement select(String param) {
        this.sqlQuery = this.sqlQuery + "SELECT " + param;
        return this;
    }

    public SqlPreparedStatement select(String[] params) {
        this.sqlQuery = this.sqlQuery + "SELECT ";
        this.length = params.length;
        this.returnInteger = 0;
        for (String param : params) {
            ++this.returnInteger;
            this.sqlQuery = this.sqlQuery + param;
            if (this.returnInteger == this.length) continue;
            this.sqlQuery = this.sqlQuery + ",";
        }
        return this;
    }

    public SqlPreparedStatement from(String table) {
        this.sqlQuery = this.sqlQuery + " FROM " + table;
        return this;
    }

    public SqlPreparedStatement from(String[] tables) {
        this.sqlQuery = this.sqlQuery + " FROM ";
        this.length = tables.length;
        this.returnInteger = 0;
        for (String table : tables) {
            ++this.returnInteger;
            this.sqlQuery = this.sqlQuery + table;
            if (this.returnInteger == this.length) continue;
            this.sqlQuery = this.sqlQuery + ",";
        }
        return this;
    }

    public SqlPreparedStatement join(String table, String joins, String position) {
        String set = "";
        this.sqlQuery = this.sqlQuery + " " + position + " JOIN " + table + " ON " + joins;
        return this;
    }

    public SqlPreparedStatement update(String table, String[] param) throws SiebelBusinessServiceException {
        try {
            this.sqlQuery = this.sqlQuery + "UPDATE " + table;
            this.set(param);
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return this;
    }

    public SqlPreparedStatement set(String[] param) throws SQLException {
        this.length = param.length;
        this.setString = "";
        this.returnInteger = 0;
        if (this.length <= 0) {
            throw new SQLException("Sql Parameter for insert statement cannot be empty.");
        }
        for (String column : param) {
            ++this.returnInteger;
            this.setString = this.setString + column + "=" + "?";
            if (this.returnInteger == this.length) continue;
            this.setString = this.setString + ",";
        }
        this.sqlQuery = this.sqlQuery + " SET " + this.setString;
        return this;
    }

    public SqlPreparedStatement where(String column) {
        this.sqlQuery = this.sqlQuery + " WHERE " + column + "=" + "?";
        return this;
    }

    public SqlPreparedStatement where(String column, String value) {
        this.sqlQuery = this.sqlQuery + " WHERE " + column + "=" + value;
        return this;
    }

    public SqlPreparedStatement andWhere(String column) {
        this.sqlQuery = this.sqlQuery + " AND " + column + "=" + "?";
        return this;
    }

    public SqlPreparedStatement andWhere(String column, String value) {
        this.sqlQuery = this.sqlQuery + " AND " + column + "=" + value;
        return this;
    }

    public SqlPreparedStatement andIsNull(String column) {
        this.sqlQuery = this.sqlQuery + " AND " + column + " " + "IS NULL";
        return this;
    }

    public ResultSet get() throws SiebelBusinessServiceException {
        try {
            rs = preparedStatement.executeQuery();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return rs;
    }

    public SqlPreparedStatement orWhere(String column) {
        this.sqlQuery = this.sqlQuery + " OR " + column + "=" + "?";
        return this;
    }

    public SqlPreparedStatement groupBy(String[] groups) {
        this.sqlQuery = this.sqlQuery + "GROUP BY ";
        this.length = groups.length;
        this.returnInteger = 0;
        for (String group : groups) {
            ++this.returnInteger;
            this.sqlQuery = this.sqlQuery + group;
            if (this.returnInteger == this.length) continue;
            this.sqlQuery = this.sqlQuery + ",";
        }
        return this;
    }

    public void close() throws SiebelBusinessServiceException {
        try {
            preparedStatement.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }

    public String getQuery() {
        String query = this.sqlQuery;
        this.sqlQuery = null;
        return query;
    }

    static {
        rs = null;
    }
}

