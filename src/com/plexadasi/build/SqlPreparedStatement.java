/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.build;

import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

/**
 *
 * @author SAP Training
 */
public class SqlPreparedStatement 
{
    static StringWriter errors = new StringWriter();
    private static Connection CONN;
    private static Statement cs;
    private static PreparedStatement preparedStatement;
    protected String sqlQuery = "";
    protected static ResultSet rs = null;
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
    
    public SqlPreparedStatement()
    {
        
    }
    
    public SqlPreparedStatement(Connection ebsConn)
    {
        CONN = ebsConn;
    }
    
    public SqlPreparedStatement preparedStatement() throws SiebelBusinessServiceException
    {
        try 
        {
            preparedStatement = CONN.prepareStatement(sqlQuery);
        } 
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception at prepared statement:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        finally{
            this.sqlQuery = "";
        }
        return this;
    }
    
   // public Object query(String query, Object[], RowMapper)
    
    public void setInt(int index, int value) throws SiebelBusinessServiceException
    {
        try 
        {
            preparedStatement.setInt(index, value);
        } 
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception when setting number:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }
    

    public void setFloat(int index, Float value) throws SiebelBusinessServiceException {
        try {
            preparedStatement.setFloat(index, value);
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception when setting string:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }
    
    public void setString(int index, String value) throws SiebelBusinessServiceException
    {
        try 
        {
            preparedStatement.setString(index, value);
        } 
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception when setting string:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }
    
    public void setArray(int index, Array value) throws SiebelBusinessServiceException
    {
        try 
        {
            preparedStatement.setArray(index, value);
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception when setting array:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }
    
    public Boolean executeUpdate() throws SiebelBusinessServiceException
    {
        returnInteger = 0;
        try 
        {
            returnInteger = preparedStatement.executeUpdate();
            if(returnInteger > 0)
            {
                CONN.commit();
                returnBoolean = true;
            }
            else
            {
                CONN.rollback();
            }
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception at execute update method:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return returnBoolean;
    }
    
    public SqlPreparedStatement select(String param)
    {
        sqlQuery += SELECT_CLAUSE + param;
        return this;
    }
    
    public SqlPreparedStatement select(String[] params)
    {
        sqlQuery += SELECT_CLAUSE;
        length = params.length;
        returnInteger = 0;
        for(String param : params)
        {
            returnInteger++;
            sqlQuery += param;
            if(returnInteger != length)
            {
                sqlQuery += COMMA_SEPERATOR;
            }
        }
        return this;
    }
    
    public SqlPreparedStatement from(String table) 
    {
        sqlQuery += FROM_CLAUSE + table;
        return this;
    }
    
    public SqlPreparedStatement from(String[] tables)
    {
        sqlQuery += FROM_CLAUSE;
        length = tables.length;
        returnInteger = 0;
        for(String table : tables)
        {
            returnInteger++;
            sqlQuery += table;
            if(returnInteger != length)
            {
                sqlQuery += COMMA_SEPERATOR;
            }
        }
        return this;
    }
    
    public SqlPreparedStatement join(String table, String joins, String position)
    {
        String set = "";
        sqlQuery += BLANK + position + " JOIN " + table + " ON " + joins;
        return this;
    }
    
    public SqlPreparedStatement update(String table, String[] param) throws SiebelBusinessServiceException
    {
        try 
        {
            sqlQuery += UPDATE_CLAUSE + table;
            set(param);
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return this;
    }
    
    public SqlPreparedStatement set(String[] param) throws SQLException
    {
        length = param.length;
        setString = "";
        returnInteger = 0;
        if(length <= 0)
        {
            throw new SQLException(PARAM_EXCEPTION);
        }
        for(String column : param)
        {
            returnInteger++;
            setString += column + EQUALS + POSITIONAL_PARAMETER;
            if(returnInteger != length)
            {
                setString += COMMA_SEPERATOR;
            }
        }
        sqlQuery += BLANK + SET_CLAUSE + setString;
        return this;
    }
    
    public SqlPreparedStatement where(String column)
    {
        sqlQuery += BLANK + WHERE_CLAUSE + column + EQUALS + POSITIONAL_PARAMETER;
        return this;
    }
    
    public SqlPreparedStatement where(String column, String value)
    {
        sqlQuery += BLANK + WHERE_CLAUSE + column + EQUALS + value;
        return this;
    }
    
    public SqlPreparedStatement andWhere(String column)
    {
        sqlQuery += BLANK + AND_CLAUSE + column + EQUALS + POSITIONAL_PARAMETER;
        return this;
    }
    
    public SqlPreparedStatement andWhere(String column, String value)
    {
        sqlQuery += BLANK + AND_CLAUSE + column + EQUALS + value;
        return this;
    }
    
    public SqlPreparedStatement andIsNull(String column)
    {
        sqlQuery += BLANK + AND_CLAUSE + column + BLANK + IS_NULL;
        return this;
    }
    
    public ResultSet get() throws SiebelBusinessServiceException
    {
        try 
        {
            rs = preparedStatement.executeQuery();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return rs;
    }
    
    public SqlPreparedStatement orWhere(String column)
    {
        sqlQuery += BLANK + OR_CLAUSE + column + EQUALS + POSITIONAL_PARAMETER;
        return this;
    }
    
    public SqlPreparedStatement groupBy(String[] groups)
    {
        sqlQuery += GROUP_BY_CLAUSE;
        length = groups.length;
        returnInteger = 0;
        for(String group : groups)
        {
            returnInteger++;
            sqlQuery += group;
            if(returnInteger != length)
            {
                sqlQuery += COMMA_SEPERATOR;
            }
        }
        return this;
    }
    
    public void close() throws SiebelBusinessServiceException
    {
        try 
        {
            preparedStatement.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }
    
    public String getQuery()
    {
        String query = this.sqlQuery;
        this.sqlQuery = null;
        return query;
    }
}
