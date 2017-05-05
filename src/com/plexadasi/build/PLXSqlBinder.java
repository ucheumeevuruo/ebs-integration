package com.plexadasi.build;


import com.plexadasi.ebs.Helper.DataTypeCheck;
import java.sql.SQLException;
import java.util.Map;





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
    
    public void setInsertParamArray(Map<String, String>  param) throws SQLException
    {
        for (Map.Entry<String, String> entry : param.entrySet()) {
            params += bindParam(entry.getValue());
        }
    }
    
    public String insertAutobindParam(String param) throws SQLException
    {
        return bindParam(param);
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