/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.services;

import com.plexadasi.ebs.CallableRunner;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbutils.QueryRunner;
//import org.apache.commons.dbutils.ResultSetHandler;
//import org.apache.commons.dbutils.handlers.ScalarHandler;

/**
 *
 * @author SAP Training
 */
public final class SiebelToEbsService {
    private final Connection connection;
    private final Object[] params;
    private final String functionName;
//    private final QueryRunner jdbcTemplateObject;
    
    /**
     * 
     * @param connection
     * @param functionName
     * @param params 
     */
    public SiebelToEbsService(Connection connection, String functionName, Object... params){
//        this.jdbcTemplateObject = new QueryRunner();
        this.connection = connection;
        this.params = params;
        this.functionName = functionName;
    }
    
    /**
     * 
     * @return
     * @throws SQLException 
     *//*
    public Object query() throws SQLException{
        ResultSetHandler<String> rsh = new ScalarHandler<String>();
        return jdbcTemplateObject.query(connection, callFunction(), rsh, params);
    }*/
    
    public Object queryProc() throws SQLException{
        CallableRunner prun = new CallableRunner();
        return prun.queryProc(connection, prepareProc(), params);
    }
    
    /**
     * 
     * @return 
     */
    private String prepareProc(){
        
        String bind = "CALL " + functionName + "(";
        int length = params.length;
        
        for(int i = 0; i < length; i++ ){
            bind += "?";
            
            if(i != length -1)
                bind += ",";
        }
        bind += ")";
        return bind;
        
    }
    
    /**
     * 
     * @return 
     *//*
    private String callFunction(){
        String bind = "select " + functionName + "(";
        int length = params.length;
        
        for(int i = 0; i < length; i++ ){
            bind += "?";
            
            if(i != length -1)
                bind += ",";
        }
        
        bind += ") from dual";
        return bind;
    }*/
}
