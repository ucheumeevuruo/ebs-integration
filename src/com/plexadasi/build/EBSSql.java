package com.plexadasi.build;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SAP Training
 */
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.ImplSql;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EBSSql {
    private ImplSql obj;
    StringWriter errors = new StringWriter();
    private static String sqlContext;
    private static Connection CONN;
    private static CallableStatement cs;
    
    public EBSSql(Connection connectObj)
    {
        CONN = connectObj;
    }
    
    public void createInvoiceOrder(ImplSql method) throws SiebelBusinessServiceException
    {
        try {
            obj = method;
            sqlContext = Context.call(obj);
            MyLogging.log(Level.INFO, "SQL for InvoiceOrder :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.execute();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", errors.toString());
        }
    }
    
    public void createInvoiceQuote(ImplSql method) throws SiebelBusinessServiceException
    {
        try {
            obj = method;
            sqlContext = Context.call(obj, true);
            MyLogging.log(Level.INFO, "SQL For InvoiceQuote :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.execute();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", errors.toString());
        }
    }
    
     public void createLocation(ImplSql method) throws SQLException
    {
        try {
            obj = method;
            sqlContext = Context.call(obj);
            MyLogging.log(Level.INFO, "SQL for Location :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.registerOutParameter(2, java.sql.Types.VARCHAR);
            cs.registerOutParameter(3, java.sql.Types.INTEGER);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.execute();
        } catch (SiebelBusinessServiceException ex) {
            Logger.getLogger(EBSSql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createPartySite(ImplSql method) throws SiebelBusinessServiceException
    {
        try {
            obj = method;
            sqlContext = Context.call(obj);
            MyLogging.log(Level.INFO, "SQL For PartySite :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.registerOutParameter(2, java.sql.Types.VARCHAR);
            cs.execute();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", errors.toString());
        }
    }
    
    public void createSiteUsage(ImplSql method) throws SQLException, SiebelBusinessServiceException
    {
        try {
            obj = method;
            sqlContext = Context.call(obj);
            MyLogging.log(Level.INFO, "SQL :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.registerOutParameter(2, java.sql.Types.VARCHAR);
            cs.execute();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", errors.toString());
        }
    }
    
    public void createCustomerSite(ImplSql method) throws SiebelBusinessServiceException
    {
        try {
            obj = method;
            sqlContext = Context.call(obj);
            MyLogging.log(Level.INFO, "SQL :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.registerOutParameter(1, java.sql .Types.INTEGER);
            cs.execute();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", errors.toString());
        }
    }
    
    public void createAccountSite(ImplSql method) throws SiebelBusinessServiceException
    {
        try {
            obj = method;
            sqlContext = Context.call(obj);
            MyLogging.log(Level.INFO, "SQL :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.registerOutParameter(1, java.sql .Types.INTEGER);
            cs.registerOutParameter(2, java.sql .Types.VARCHAR);
            cs.execute();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", errors.toString());
        }
    }
    
    public String getString(int value) throws SQLException
    {
        return cs.getString(value);
    }
    
    public Integer getInt(int value) throws SQLException
    {
        return cs.getInt(value);
    }
    
    public Date get(String value) throws SQLException
    {
        return cs.getDate(value);
    }
}