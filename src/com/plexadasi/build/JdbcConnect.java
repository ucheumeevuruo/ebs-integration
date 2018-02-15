/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.build;

import com.plexadasi.ebs.services.mapper.RowMapper;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SAP Training
 */
public class JdbcConnect 
{
    private final Connection connection;
    private PreparedStatement preparedStatement;
    private String sqlQuery;
    private Object[] object;
    private ResultSet rs;
    
    public JdbcConnect(Connection connection)
    {
        this.connection = connection;
    }
    
    public Object queryForObject(SqlPreparedStatement sqlQuery, Object[] object, RowMapper rowMapper) throws SiebelBusinessServiceException
    {
        return this.queryForObject(sqlQuery.getQuery(), object, rowMapper);
    }
    
    public <T> Object queryForObject(String sqlQuery, Object[] object, RowMapper rowMapper) throws SiebelBusinessServiceException
    {
        Object result = new Object();
        try {
            this.sqlQuery = sqlQuery;
            this.object = object;
            this.rs = this.prepareQuery().executeQuery();
            while(this.rs.absolute(0))
            {
                result = rowMapper.mapRow(this.rs, 0);
            }
            this.rs.close();
        } catch (SQLException ex) {
            throw new SiebelBusinessServiceException("JDBC", "Cannot connect to service");
        }
        return result;
    }
    
    public List query(String sqlQuery, Object[] object, RowMapper rowMapper) throws SiebelBusinessServiceException
    {
         List result = new ArrayList();
        try {
            this.sqlQuery = sqlQuery;
            this.object = object;
            this.rs = this.prepareQuery().executeQuery();
            while(this.rs.next())
            {
                result.add(rowMapper.mapRow(this.rs, 0));
            }
            this.rs.close();
        } catch (SQLException ex) {
            throw new SiebelBusinessServiceException("JDBC", "Cannot connect to service");
        }
        return result;
    }
    
    private PreparedStatement prepareQuery() throws SiebelBusinessServiceException, SQLException{
        try {
            this.preparedStatement = this.connection.prepareCall(this.sqlQuery);
            for(int i = 0; i < this.object.length; i++)
            {
                this.preparedStatement.setObject(i + 1, this.object[i]);
            }
        } catch(NullPointerException ex){
            throw new SiebelBusinessServiceException("JDBC", "No connection object found");
        }
        return this.preparedStatement;
    }

    public List query(String SQL, Object[] object) throws SiebelBusinessServiceException {
         List result = new ArrayList();
        try {
            this.object = object;
            while(this.prepareQuery().executeQuery().next())
            {
                result.add(rs.getObject(SQL));
            }
            this.rs.close();
        } catch (SQLException ex) {
            throw new SiebelBusinessServiceException("JDBC", "Cannot connect to service");
        }
        return result;
    }
}
