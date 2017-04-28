/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.build.objects;

import java.sql.SQLException;

/**
 *
 * @author SAP Training
 */
public class PLXSql {
    private final String InsertInto = "INSERT INTO";
    
    private static final String BRACKET_OPEN = "(";
    
    private static final String BRACKET_CLOSE = ")";
    
    private static final String POSITIONAL_PARAMETER = "?";
    
    private static final String VALUES = "VALUES";
    
    private static final String WHITE_SPACE = " ";
    
    
    private String[] params = new String[]{};
    
    private String stringOutput;
    
    private String positionalParam;
    
    private static final String PARAM_EXCEPTION = "Sql Parameter for insert statement cannot be empty.";
    
    private String createParameter() throws SQLException
    {
        int num = 0, length = params.length;
        if(length <= 0)
        {
            throw new SQLException(PARAM_EXCEPTION);
        }
        for(String entry : params)
        {
            stringOutput += entry;
            positionalParam += POSITIONAL_PARAMETER;
            if(num++ != length - 1)
            {
                stringOutput += positionalParam += ", ";
            }
        }
        return stringOutput;
    }
    
    public void setParams(String[] param)
    {
        params = param;
    }
    
    public String insertStatement() throws SQLException
    {
        stringOutput = InsertInto + BRACKET_OPEN + createParameter() + BRACKET_CLOSE;
        stringOutput += WHITE_SPACE;
        stringOutput += VALUES + BRACKET_OPEN + positionalParam + BRACKET_CLOSE;
        return stringOutput;
    }
    
    public String bindParam() throws SQLException
    {
        PLXSqlBinder bind = new PLXSqlBinder();
        bind.setInsertParamArray(params);
        stringOutput = bind.getInsertParam();
        return stringOutput;
    }
}
