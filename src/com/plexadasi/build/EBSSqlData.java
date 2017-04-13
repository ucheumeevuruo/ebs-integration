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
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;


public class EBSSqlData 
{
    StringWriter errors = new StringWriter();
    private static Connection CONN;
    private static Statement cs;
    private static PreparedStatement preparedStatement;
    
    public EBSSqlData(Connection connectObj)
    {
        CONN = connectObj;
    }
    
    public String getTrxInvoiceHeader() throws SiebelBusinessServiceException
    {
        String output = "";
        String sql = "SELECT xx_invoice_header.nextval as num FROM DUAL";
        try {
            MyLogging.log(Level.INFO, "SQL For PartySite :" + sql);
            cs = CONN.createStatement();
            ResultSet rs = cs.executeQuery(sql);
            while (rs.next()) {
                output = String.valueOf(rs.getInt("num")); 
                MyLogging.log(Level.INFO, "TRX_HEADER_ID:{0}"+ output);
            } 
            cs.close();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", errors.toString());
        }
        return output;
    }
    
    public String getTrxLineId() throws SiebelBusinessServiceException
    {
        String output = "";
        String sql = "SELECT xx_invoice_line.NEXTVAL as num FROM DUAL";
        try {
            MyLogging.log(Level.INFO, "SQL For PartySite :" + sql);
            cs = CONN.createStatement();
            ResultSet rs = cs.executeQuery(sql);
            while (rs.next()) {
                output = String.valueOf(rs.getInt("num")); 
                MyLogging.log(Level.INFO, "TRX_LINE_ID:{0}"+ output);
            } 
            cs.close();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", errors.toString());
        }
        return output;
    }
    
    public String getTrxDistId() throws SiebelBusinessServiceException
    {
        String output = "";
        String sql = "SELECT xx_invoice_dist.NEXTVAL as num FROM DUAL;";
        try {
            MyLogging.log(Level.INFO, "SQL For PartySite :" + sql);
            cs = CONN.createStatement();
            ResultSet rs = cs.executeQuery(sql);
            while (rs.next()) {
                output = String.valueOf(rs.getInt("num")); 
                MyLogging.log(Level.INFO, "TRX_DIST_ID:{0}"+ output);
            } 
            cs.close();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", errors.toString());
        }
        return output;
    }
    
    public String getTrxNumber(int cust_trx_id) throws SiebelBusinessServiceException
    {
        String output = null;
        try {
            String sql = "select trx_number from RA_CUSTOMER_TRX_ALL where customer_trx_id = ?";
            MyLogging.log(Level.INFO, "SQL For PartySite :" + sql);
            preparedStatement = CONN.prepareStatement(sql);
            preparedStatement.setInt(1, cust_trx_id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                output = String.valueOf(rs.getInt("trx_number"));
                MyLogging.log(Level.INFO, "TRX_NUMBER:{0}"+ output);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", errors.toString());
        }
        return output;
    }
}