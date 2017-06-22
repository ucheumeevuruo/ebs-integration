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
import com.plexadasi.ebs.Helper.DataConverter;
import com.plexadasi.ebs.Helper.HelperAP;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.ImplSql;
import com.plexadasi.order.PurchaseOrderInventory;
import com.plexadasi.order.SalesOrderInventory;
import com.siebel.data.SiebelDataBean;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleTypes;


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
        try 
        {
            obj = method;
            sqlContext = Context.call(obj);
            MyLogging.log(Level.INFO, "SQL for InvoiceOrder :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.execute();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
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
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }
    
     public void createLocation(ImplSql method) throws SQLException, SiebelBusinessServiceException
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
        } 
        catch (SiebelBusinessServiceException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
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
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);
            cs.registerOutParameter(4, java.sql.Types.INTEGER);
            cs.registerOutParameter(5, java.sql.Types.VARCHAR);
            cs.execute();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
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
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);
            cs.registerOutParameter(4, java.sql.Types.INTEGER);
            cs.registerOutParameter(5, java.sql.Types.VARCHAR);
            cs.execute();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
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
            cs.registerOutParameter(2, java.sql.Types.VARCHAR);
            cs.registerOutParameter(3, java.sql.Types.INTEGER);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.execute();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
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
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);
            cs.registerOutParameter(4, java.sql.Types.INTEGER);
            cs.registerOutParameter(5, java.sql.Types.VARCHAR);
            cs.execute();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }
    
    public void createSalesOrder(SiebelDataBean sb, SalesOrderInventory salesOrder) throws SiebelBusinessServiceException
    {
        try 
        {
            cs = CONN.prepareCall("{CALL SALES_ORDER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            EBSSqlData ebsData = new EBSSqlData(CONN);
            Integer orgId = salesOrder.getSoldToOrgId();
            String shipToOrgId = ebsData.shipToAccount(orgId);
            String billToOrgId = ebsData.billToAccount(orgId);
            MyLogging.log(Level.INFO, shipToOrgId);
            // Pass the in parameters to the procedure call
            cs.setInt(1, DataConverter.toInt(HelperAP.getEbsUserId()));
            cs.setInt(2, DataConverter.toInt(HelperAP.getEbsUserResp()));
            cs.setInt(3, salesOrder.getOrderId());  
            cs.setInt(4, orgId);
            cs.setInt(5, DataConverter.toInt(shipToOrgId)); 
            cs.setInt(6, DataConverter.toInt(billToOrgId));
            cs.setInt(7, salesOrder.getSoldFromId());
            cs.setInt(8, salesOrder.getSalesRepId());
            cs.setInt(9, DataConverter.toInt(HelperAP.getPriceListID()));
            cs.setString(10, salesOrder.getTransactionCode());
            cs.setString(11, salesOrder.getStatusCode());
            cs.setString(12, String.valueOf(salesOrder.getSiebelOrderId()));
            cs.setInt(13, salesOrder.getSourceId());
            cs.setArray(14, salesOrder.inventory(sb, CONN).getInventoryItem());
            // Retrieve the out parameters from the procedure call
            cs.registerOutParameter(15, java.sql .Types.VARCHAR);
            cs.registerOutParameter(16, java.sql .Types.VARCHAR);
            cs.registerOutParameter(17, java.sql .Types.INTEGER);
            
            // Bind the output array, this will contain any exception indexes.
            cs.registerOutParameter(18, OracleTypes.ARRAY, "CUSTOMERS_ARRAY");
            cs.registerOutParameter(19, java.sql .Types.VARCHAR);
            cs.registerOutParameter(20, OracleTypes.CHAR);
            cs.execute();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        } 
    }
    
    public void createOrderReservation(Integer order_number) throws SiebelBusinessServiceException
    {
        try 
        {
            sqlContext = "{CALL ORDER_MGMT_RESERVATION(?,?,?,?)}";
            MyLogging.log(Level.INFO, "SQL :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.setInt(1, order_number);
            cs.registerOutParameter(2, java.sql .Types.VARCHAR);
            cs.registerOutParameter(3, java.sql .Types.VARCHAR);
            cs.registerOutParameter(4, OracleTypes.ARRAY, "CUSTOMERS_ARRAY");
            cs.execute();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }
    
    public void cancelSalesOrder(String header_id) throws SiebelBusinessServiceException{
        try {
            cs = CONN.prepareCall("{CALL CANCEL_SALES_ORDER(?,?,?,?,?,?,?,?)}");
            cs.setInt(1, DataConverter.toInt(HelperAP.getEbsUserId()));
            cs.setInt(2, DataConverter.toInt(HelperAP.getEbsUserResp()));
            cs.setString(3, header_id);
            cs.registerOutParameter(4, java.sql .Types.VARCHAR);
            cs.registerOutParameter(5, java.sql .Types.INTEGER);
            cs.registerOutParameter(6, java.sql .Types.VARCHAR);
            cs.registerOutParameter(7, java.sql .Types.VARCHAR);
            cs.registerOutParameter(8, OracleTypes.ARRAY, "CUSTOMERS_ARRAY");
            cs.execute();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }
    
    public void cancelSalesLineOrder(String header_id, String line_id, String quantity_ordered) throws SiebelBusinessServiceException{
        try {
            cs = CONN.prepareCall("{CALL CANCEL_LINE_ORDER(?,?,?,?,?,?,?,?,?)}");
            cs.setInt(1, DataConverter.toInt(HelperAP.getEbsUserId()));
            cs.setInt(2, DataConverter.toInt(HelperAP.getEbsUserResp()));
            cs.setInt(3, DataConverter.toInt(header_id));
            cs.setInt(4, DataConverter.toInt(line_id));
            cs.setInt(5, DataConverter.toInt(quantity_ordered));
            cs.registerOutParameter(6, java.sql .Types.VARCHAR);
            cs.registerOutParameter(7, java.sql .Types.INTEGER);
            cs.registerOutParameter(8, java.sql .Types.VARCHAR);
            cs.registerOutParameter(9, OracleTypes.ARRAY, "CUSTOMERS_ARRAY");
            cs.execute();
            MyLogging.log(Level.SEVERE, header_id + " " + line_id + " " + quantity_ordered);
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }
    
    public void createPurchaseOrder(SiebelDataBean sb, PurchaseOrderInventory poInventory) throws SiebelBusinessServiceException
    {
        sqlContext = "{CALL PURCHASE_ORDER(?,?,?,?,?,?,?)}";
        try 
        {
            String shipToCity = poInventory.getShipToCity() != null ? " " + poInventory.getShipToCity() + "," : "";
            String shipToState = poInventory.getShipToState() != null ? poInventory.getShipToState() : "";
            String shipToCountry = poInventory.getShipToCountry() != null ? poInventory.getShipToCountry() : "";
            String billToCity = poInventory.getBillToCity() != null ? " " + poInventory.getBillToCity() + "," : "";
            String billToState = poInventory.getBillToState() != null ? poInventory.getBillToState() : "";
            String billToCountry = poInventory.getBillToCountry() != null ? poInventory.getBillToCountry() : "";
            MyLogging.log(Level.INFO, "SQL :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.setInt(1, poInventory.getSourceId());
            cs.setString(2, 
                poInventory.getShipToLocation() + 
                shipToCity + " " + 
                shipToState
            );
            cs.setString(3, 
                poInventory.getBillToLocation() + 
                billToCity + " " + 
                billToState
            );
            cs.setString(4, poInventory.getCurrencyCode());
            cs.setInt(5, poInventory.getAgentCode());
            cs.setString(6, poInventory.getOrderNumber());
            cs.setArray(7, poInventory.inventory(sb, CONN).getInventoryItem());
            MyLogging.log(Level.INFO, poInventory.toString());
            //cs.registerOutParameter(2, java.sql .Types.VARCHAR);
            //cs.registerOutParameter(3, java.sql .Types.VARCHAR);
            //cs.registerOutParameter(4, OracleTypes.ARRAY, "CUSTOMERS_ARRAY");
            cs.execute();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
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
    
    public Date getDate(String value) throws SQLException
    {
        return cs.getDate(value);
    }
    
    public long getLong(int value) throws SQLException
    {
        return cs.getLong(value);
    }
    
    public Array getArray(int value) throws SQLException
    {
        return cs.getArray(value);
    }
}