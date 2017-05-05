/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.build;

import com.plexadasi.ebs.Helper.DataTypeCheck;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author SAP Training
 */
public class PLXSql {
    private final String InsertInto = "INSERT INTO ";
    
    private static final String BRACKET_OPEN = "(";
    
    private static final String BRACKET_CLOSE = ")";
    
    private static final String POSITIONAL_PARAMETER = "?";
    
    private static final String VALUES = "VALUES";
    
    private static final String WHITE_SPACE = " ";
    
    
    private Map<String, String> params = new HashMap();
    
    private String stringOutput;
    
    private String positionalParam;
    
    private String bindParam;
    
    PLXSqlBinder bind = new PLXSqlBinder();
    
    private PreparedStatement ps = null;
    
    private Connection Conn = null;
    
    private static final String PARAM_EXCEPTION = "Sql Parameter for insert statement cannot be empty.";
    
    public PLXSql(Connection conn)
    {
        Conn = conn;
    }
    
    private String createParameter() throws SQLException
    {
        int num = 0, length = params.size();
        if(length <= 0)
        {
            throw new SQLException(PARAM_EXCEPTION);
        }
        
        for(Map.Entry<String, String> entry : params.entrySet())
        {
            String value = entry.getValue();
            stringOutput += value;
            positionalParam += POSITIONAL_PARAMETER;
            if(num++ != length - 1)
            {
                stringOutput += positionalParam += ", ";
            }
            bindParam += bind.insertAutobindParam(value);
        }
        return stringOutput;
    }
    
    public void setParams(Map<String, String> param)
    {
        params = param;
    }
    
    private String sqlQuery(String table) throws SQLException
    {
        stringOutput = InsertInto + table + BRACKET_OPEN + createParameter() + BRACKET_CLOSE;
        stringOutput += WHITE_SPACE;
        stringOutput += VALUES + BRACKET_OPEN + positionalParam + BRACKET_CLOSE;
        return stringOutput;
    }
    
    public void preparedStatement(String table) throws SQLException
    {
        ps = Conn.prepareStatement(sqlQuery(table));
    }
    
    public void executeStatement() throws SQLException
    {
        boolean execute = ps.execute();
        if(!execute)
        {
            throw new SQLException("Failed to execute query");
        }
    }
    
    public void setPrepare(PreparedStatement ps) throws SQLException
    {
        int num = 0;
        for(Map.Entry<String, String> entry : params.entrySet())
        {
            num += num;
            String key = entry.getKey();
            if(DataTypeCheck.isDoubleInt(0))
            {
                ps.setDouble(num, Double.valueOf(key));
            }
            else if(DataTypeCheck.isStringInt(key))
            {
                ps.setInt(num, Integer.valueOf(key));
            }
            else if(DataTypeCheck.isObjectInteger(key))
            {
                ps.setInt(num, Integer.valueOf(key));
            }
            else if(DataTypeCheck.isString(key))
            {
                ps.setString(num, key);
            }
            else
            {
                throw new SQLException("Data type is incompatible");
            }
        }
    }
}
