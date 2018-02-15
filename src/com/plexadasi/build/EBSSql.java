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
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.Helper.DataConverter;
import com.plexadasi.Helper.HelperAP;
import com.plexadasi.build.Context;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.bin.Inventory;
import com.plexadasi.ebs.SiebelApplication.bin.Quote;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.ImplSql;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.plexadasi.ebs.model.BillingAccount;
import com.plexadasi.ebs.model.Customer;
import com.plexadasi.ebs.services.BillingAccountService;
import com.plexadasi.invoice.InvoiceObject;
import com.plexadasi.order.PurchaseOrderInventory;
import com.plexadasi.order.SalesOrderInventory;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleTypes;
import static oracle.jdbc.OracleTypes.STRUCT;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;


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
    /*
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
    */
    public void createInvoiceQuote(SiebelDataBean sConn, InvoiceObject invoice, Quote product) throws SiebelBusinessServiceException
    {
        try {
            sqlContext = "{CALL CREATE_SINGLE_INVOICE(?,?,?,?,?,?,?,?,?,?,?,?)}";
            MyLogging.log(Level.INFO, "SQL :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            // Get the Ebs User Id
            cs.setInt(1, DataConverter.toInt(HelperAP.getEbsUserId()));
            // Get the Ebs User Resposibility
            cs.setInt(2, DataConverter.toInt(HelperAP.getEbsUserResp()));
            // Get the Ebs Application Id
            cs.setInt(3, DataConverter.toInt(HelperAP.getEbsAppId()));
            // Get the batch source id
            cs.setInt(4, DataConverter.toInt(HelperAP.getSourceBatchId()));
            cs.setInt(5, DataConverter.toInt(invoice.getBillToId()));
            cs.setInt(6, DataConverter.toInt(invoice.getCustomerTrxTypeId()));
            cs.setString(7, invoice.getTrxCurrency());
            cs.setString(8, invoice.getTermId());
            cs.setInt(9, DataConverter.toInt(HelperAP.getLegalEntity()));
            cs.setString(10, invoice.getPrimarySalesId());
            cs.setArray(11, new ARRAY(
                ArrayDescriptor.createDescriptor("ITEM", CONN), 
                CONN, 
                createStructArray(
                    product.getInventories(CONN), 
                    StructDescriptor.createDescriptor("ITEMS", CONN), 
                    CONN
                )
            ));
            cs.registerOutParameter(12, java.sql .Types.VARCHAR);
            cs.execute();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQLException", ex.getMessage());
        }
    }
    
    public STRUCT[] createStructArray(List<Inventory> inventory, StructDescriptor structDescriptor, Connection ebsConn) throws SQLException
    {
        STRUCT[] structArray = new STRUCT[inventory.size()];
        Integer index = 0;
        for(Inventory inventories : inventory)
        {
            Object[] structObj = new Object[]{
                inventories.getPart_number(),
                inventories.getOrg_id(),
                inventories.getQuantity(),
                inventories.getAmount(),
                inventories.getLine_type()
            };
            STRUCT genericStruct = new STRUCT(structDescriptor, ebsConn, structObj);
            structArray[index] = genericStruct;
            index++;
        }
        return structArray;
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
    
    public void createSalesOrder(SiebelDataBean sb, SalesOrderInventory salesOrder) throws SiebelBusinessServiceException, SiebelException
    {
        try 
        {
            // Initialize the procedure
            cs = CONN.prepareCall("{CALL SALES_ORDER_TEST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            
            // Update the prices in EBS before we proceed
            salesOrder.inventory(sb, CONN).updatePriceList();
            
            // Pass the in parameters to the procedure call
            cs.setInt(1, DataConverter.toInt(HelperAP.getEbsUserId()));
            cs.setInt(2, DataConverter.toInt(HelperAP.getEbsUserResp()));
            cs.setInt(3, salesOrder.getOrderId());  
            cs.setInt(4, salesOrder.getSoldToOrgId());
            cs.setInt(5, salesOrder.getShipToId());
            cs.setInt(6, salesOrder.getBillToId());
            cs.setInt(7, salesOrder.getSoldFromId());
            cs.setInt(8, salesOrder.getSalesRepId());
            cs.setInt(9, DataConverter.toInt(HelperAP.getPriceListID()));
            cs.setString(10, salesOrder.getTransactionCode());
            cs.setString(11, salesOrder.getStatusCode());
            cs.setString(12, String.valueOf(salesOrder.getSiebelOrderId()));
            cs.setInt(13, salesOrder.getSourceId());
            cs.setArray(14, salesOrder.inventory(sb, CONN).getInventoryItem());
            // Retrieve the out parameters from the procedure call
            //cs.registerOutParameter(15, java.sql .Types.VARCHAR);
            //cs.registerOutParameter(16, java.sql .Types.VARCHAR);
            //cs.registerOutParameter(17, java.sql .Types.INTEGER);
            
            // Bind the output array, this will contain any exception indexes.
            //cs.registerOutParameter(18, OracleTypes.ARRAY, "CUSTOMERS_ARRAY");
            //cs.registerOutParameter(19, java.sql .Types.VARCHAR);
            cs.registerOutParameter(15, OracleTypes.CHAR);
            Boolean ex = cs.execute();
            MyLogging.log(Level.INFO, String.valueOf(ex));
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
            MyLogging.log(Level.SEVERE, errors.toString());
            throw new SiebelBusinessServiceException("SQL", ex.getMessage());
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
        sqlContext = "{CALL PURCHASE_ORDER(?,?,?,?,?,?)}";
        try 
        {
            EBSSqlData ebsData = new EBSSqlData(CONN);
            if(!ebsData.vendorExists(poInventory.getSourceId()))
            {
                MyLogging.log(Level.SEVERE, "Please choose a valid supplier.");
                throw new SiebelBusinessServiceException("NOT_FOUND", "Please choose a valid supplier.");
            }
            MyLogging.log(Level.INFO, "SQL :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            // Get the Suppliers Id
            cs.setInt(1, poInventory.getSourceId());
            // Get the Lot Id
            cs.setString(2, poInventory.getShipToLocation());
            // Get company currency code
            cs.setString(3, poInventory.getCurrencyCode());
            // Get agent id
            cs.setInt(4, poInventory.getAgentCode());
            // Get Order number
            cs.setString(5, poInventory.getOrderNumber());
            // Get array with inventory items
            cs.setArray(6, poInventory.inventory(sb, CONN).getInventoryItem());
            // Lets check if the inventory has data
            // For debug purpose
            MyLogging.log(Level.INFO, poInventory.toString());
            cs.execute();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }  
    }
    
    public void CreateItemInEBS(String desc, String part) throws SiebelBusinessServiceException
    {
        sqlContext = "{CALL CREATE_UPDATE_ITEM(?,?,?,?,?,?,?,?,?)}";
        try 
        {
            MyLogging.log(Level.INFO, "SQL :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            // Get the Ebs User Id
            cs.setInt(1, DataConverter.toInt(HelperAP.getEbsUserId()));
            // Get the Ebs User Resposibility
            cs.setInt(2, DataConverter.toInt(HelperAP.getEbsUserResp()));
            // Get the Ebs Application Id
            cs.setInt(3, DataConverter.toInt(HelperAP.getEbsAppId()));
            // Get Part Name
            cs.setString(4, part);
            // Get Description
            cs.setString(5, desc);
            // Get Lot Organization
            cs.setString(6, HelperAP.getMasterOrganizationCode());
            // Get Template Name
            cs.setString(7, HelperAP.getTemplateName());
            cs.registerOutParameter(8, java.sql .Types.INTEGER);
            cs.registerOutParameter(9, java.sql .Types.INTEGER);
            // Execute query
            cs.execute();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }  
    }
    
    public void ItemAssignToChild(Integer invId, String orgId) throws SiebelBusinessServiceException
    {
        sqlContext = "{CALL ITEM_ASSIGN(?,?,?,?,?,?)}";
        try 
        {
            MyLogging.log(Level.INFO, "SQL :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            // Get the Ebs User Id
            cs.setInt(1, DataConverter.toInt(HelperAP.getEbsUserId()));
            // Get the Ebs User Resposibility
            cs.setInt(2, DataConverter.toInt(HelperAP.getEbsUserResp()));
            // Get the Ebs Application Id
            cs.setInt(3, DataConverter.toInt(HelperAP.getEbsAppId()));
            // Get Part Name
            cs.setInt(4, invId);
            // Get Description
            cs.setString(5, orgId);
            cs.registerOutParameter(6, java.sql .Types.CLOB);
            // Execute query
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