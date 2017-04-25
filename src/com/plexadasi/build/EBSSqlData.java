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
import java.util.logging.Logger;


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
            MyLogging.log(Level.INFO, "SQL For Trx Invoice Header :" + sql);
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
            MyLogging.log(Level.INFO, "SQL For Trx Line Id :" + sql);
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
        String sql = "SELECT xx_invoice_dist.NEXTVAL as num FROM DUAL";
        try {
            MyLogging.log(Level.INFO, "SQL For Trx Dist Id :" + sql);
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
    
    public String getCombinationId(int inventory_id, int org_id) throws SiebelBusinessServiceException
    {
        String output = "";
        try {
            
            String sql = "SELECT SALES_ACCOUNT FROM MTL_SYSTEM_ITEMS WHERE INVENTORY_ITEM_ID = ? AND ORGANIZATION_ID = ?";
            MyLogging.log(Level.INFO, "SQL For Combination ID :" + sql);
            preparedStatement = CONN.prepareStatement(sql);
            preparedStatement.setInt(1, inventory_id);
            preparedStatement.setInt(2, org_id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                output = String.valueOf(rs.getInt("SALES_ACCOUNT"));
                MyLogging.log(Level.INFO, "Code Combination Number:{0}"+ output);
            }
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
            MyLogging.log(Level.INFO, "SQL For Trx Number :" + sql);
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
    
    public Boolean setCustReference(int cust_trx_id, String quote_id) throws SiebelBusinessServiceException
    {
        Boolean output = false;
        try{
            String sql = "UPDATE RA_CUSTOMER_TRX_ALL SET CT_REFERENCE=? WHERE CUSTOMER_TRX_ID=?";
            preparedStatement = CONN.prepareStatement(sql);
            preparedStatement.setString(1, quote_id);
            preparedStatement.setInt(2, cust_trx_id);
            int rs = preparedStatement.executeUpdate();
            if(rs > 0)
            {
                output = true;
            }
            preparedStatement.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", errors.toString());
        }
        MyLogging.log(Level.INFO, "Check if update is successful: " + output);
        return output;
    }
}