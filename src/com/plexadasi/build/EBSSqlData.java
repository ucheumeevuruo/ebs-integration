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
import com.plexadasi.ebs.Helper.FileToText;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.File;
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
    static StringWriter errors = new StringWriter();
    private static Connection CONN;
    private static Statement cs;
    private static PreparedStatement preparedStatement;
    private static final String OS = System.getProperty("os.name").toLowerCase();
    
    public EBSSqlData(Connection connectObj)
    {
        CONN = connectObj;
    }
    
    public String getPartySiteId(String ebs_id) throws SiebelBusinessServiceException
    {
        String output = "";
        String sql = "SELECT hca.party_id, hp.party_name\n" +
                    "FROM hz_cust_accounts hca, hz_parties hp\n" +
                    "WHERE hca.party_id = hp.party_id\n" +
                    "AND hca.cust_account_id = ?";
        try 
        {
            MyLogging.log(Level.INFO, "SQL For Trx Invoice Header :" + sql);
            preparedStatement = CONN.prepareCall(sql);
            preparedStatement.setString(1, ebs_id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                output = rs.getString("party_id"); 
                MyLogging.log(Level.INFO, "TRX_HEADER_ID:{0}"+ output);
            } 
            preparedStatement.close();
        }  
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        catch(NullPointerException ex)
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return output;
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
        } 
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        catch(NullPointerException ex)
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
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
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
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
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
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
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return output;
    }
    
    public boolean updatePriceList(int item_price, int org_id, int inventory_id, int price_list) throws SiebelBusinessServiceException
    {
        boolean output = false;
        try 
        {
            FileToText file = new FileToText();
            String sqlContext = file.convertFileToText("sql" + File.separator + "update_price_list.sql").getStringOutput();
            //MyLogging.log(Level.INFO, "SQL For Combination ID :" + sqlContext);
            preparedStatement = CONN.prepareStatement(sqlContext);
            preparedStatement.setInt(1, item_price);
            preparedStatement.setInt(2, org_id);
            preparedStatement.setInt(3, inventory_id);
            preparedStatement.setInt(4, price_list);
            int rs = preparedStatement.executeUpdate();
            if(rs > 0)
            {
                CONN.commit();
                output = true;
            }
            else
            {
                CONN.rollback();
            }
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
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
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
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
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        MyLogging.log(Level.INFO, "Check if update on ct_ref is successful: " + output);
        return output;
    }
}