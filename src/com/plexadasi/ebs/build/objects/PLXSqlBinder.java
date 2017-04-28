package com.plexadasi.ebs.build.objects;


import com.plexadasi.ebs.Helper.DataTypeCheck;
import java.sql.SQLException;





/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SAP Training
 */
public class PLXSqlBinder
{
    String stringOutput = new String();
    
    String params = new String(); 
    
    public static final String STRING = "s";
    
    public static final String INTEGER = "i";
    
    private static final String BIND_EXCEPTION = "Data type not supported.";
    
    public void setInsertParamArray(String[] param) throws SQLException
    {
        params = insertAutobindParam(param);
    }
    
    private String insertAutobindParam(String[] param) throws SQLException
    {
        for (String bind : param) {
            stringOutput += bindParam(bind);
        }
        return stringOutput;
    }
    
    private String bindParam(String bind) throws SQLException
    {
        if(DataTypeCheck.isString(bind))
        {
            stringOutput = STRING;
        }
        else if(DataTypeCheck.isObjectInteger(bind) || DataTypeCheck.isStringInt(bind))
        {
            stringOutput = INTEGER;
        }
        else
        {
            throw new SQLException(BIND_EXCEPTION);
        }
        return stringOutput;
    }
    
    public String getInsertParam()
    {
        return params;
    }
}