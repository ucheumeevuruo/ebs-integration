/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelException
 *  com.siebel.eai.SiebelBusinessServiceException
 *  oracle.sql.ARRAY
 *  oracle.sql.ArrayDescriptor
 *  oracle.sql.STRUCT
 *  oracle.sql.StructDescriptor
 */
package com.plexadasi.build;

import com.plexadasi.helper.DataConverter;
import com.plexadasi.helper.HelperAP;
import com.plexadasi.build.Context;
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.bin.Inventory;
import com.plexadasi.ebs.SiebelApplication.bin.POInventory;
import com.plexadasi.ebs.SiebelApplication.bin.Quote;
import com.plexadasi.ebs.SiebelApplication.bin.SalesOrderInventory;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.ImplSql;
import com.plexadasi.invoice.InvoiceObject;
import com.plexadasi.order.PurchaseOrderInventory;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
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

    public EBSSql(Connection connectObj) {
        CONN = connectObj;
    }

    public void createInvoiceQuote(SiebelDataBean sConn, InvoiceObject invoice, Quote product) throws SiebelBusinessServiceException {
        try {
            sqlContext = "{CALL CREATE_SINGLE_INVOICE(?,?,?,?,?,?,?,?,?,?,?,?)}";
            MyLogging.log(Level.INFO, "SQL :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.setInt(1, DataConverter.toInt(HelperAP.getEbsUserId()));
            cs.setInt(2, DataConverter.toInt(HelperAP.getEbsReceivableManagerResp()));
            cs.setInt(3, DataConverter.toInt(HelperAP.getEbsAppId()));
            cs.setInt(4, DataConverter.toInt(HelperAP.getSourceBatchId()));
            cs.setInt(5, DataConverter.toInt(invoice.getBillToId()));
            cs.setInt(6, DataConverter.toInt(invoice.getCustomerTrxTypeId()));
            cs.setString(7, invoice.getTrxCurrency());
            cs.setString(8, invoice.getTermId());
            cs.setInt(9, DataConverter.toInt(HelperAP.getLegalEntity()));
            cs.setString(10, invoice.getPrimarySalesId());
            ArrayList<Inventory> inventory = new ArrayList<Inventory>();
            Inventory inventories = new Inventory();
            Quote.PartListItems quoteItem = product.getPartQuoteItem();
            quoteItem.setSiebelAccountId(product.getSiebelAccountId());
            quoteItem.setWarehouse(invoice.getWarehouseId());
            Quote.VehicleListItems vehicleQuoteItem = product.getVehicleQuoteItem();
            vehicleQuoteItem.setSiebelAccountId(product.getSiebelAccountId());
            vehicleQuoteItem.setWarehouse(invoice.getWarehouseId());
            inventory.addAll(quoteItem.getInventories(CONN));
            inventory.addAll(vehicleQuoteItem.getInventories(CONN));
            if (invoice.getLocalDeliveryCharges().floatValue() > 0.0f) {
                inventories.setPart_number(HelperAP.getDeliveryCharges());
                inventories.setOrg_id(DataConverter.toInt(HelperAP.getLagosWarehouseId()));
                inventories.setQuantity(1);
                inventories.setAmount(invoice.getLocalDeliveryCharges());
                inventories.setLine_type("LINE");
                inventory.add(inventories);
            }
            if (invoice.getFreight().floatValue() > 0.0f) {
                inventories = new Inventory();
                inventories.setPart_number(HelperAP.getFreightCharges());
                inventories.setOrg_id(DataConverter.toInt(HelperAP.getLagosWarehouseId()));
                inventories.setQuantity(1);
                inventories.setAmount(invoice.getFreight());
                inventories.setLine_type("LINE");
                inventory.add(inventories);
            }
            if (invoice.getSundries().floatValue() > 0.0f) {
                inventories = new Inventory();
                inventories.setPart_number(HelperAP.getSundries());
                inventories.setOrg_id(DataConverter.toInt(HelperAP.getLagosWarehouseId()));
                inventories.setQuantity(1);
                inventories.setAmount(invoice.getSundries());
                inventories.setLine_type("LINE");
                inventory.add(inventories);
            }
            if (invoice.getWithholdingTax().floatValue() > 0.0f) {
                inventories = new Inventory();
                inventories.setPart_number(HelperAP.getWithholdingTax());
                inventories.setOrg_id(DataConverter.toInt(HelperAP.getLagosWarehouseId()));
                inventories.setQuantity(1);
                inventories.setAmount(invoice.getWithholdingTax());
                inventories.setLine_type("LINE");
                inventory.add(inventories);
            }
            if (invoice.getComputerProgramming().floatValue() > 0.0f) {
                inventories = new Inventory();
                inventories.setPart_number(HelperAP.getComputerProgramming());
                inventories.setOrg_id(DataConverter.toInt(HelperAP.getLagosWarehouseId()));
                inventories.setQuantity(1);
                inventories.setAmount(invoice.getComputerProgramming());
                inventories.setLine_type("LINE");
                inventory.add(inventories);
            }
            MyLogging.log(Level.INFO, "Invoice Header Description: \n" + invoice.toString());
            MyLogging.log(Level.INFO, "Describe Sales Order Inventory Object: \n" + inventory.toString());
            cs.setArray(11, (Array)new ARRAY(ArrayDescriptor.createDescriptor((String)"ITEM", (Connection)CONN), CONN, (Object)this.createStructArray(inventory, StructDescriptor.createDescriptor((String)"ITEMS", (Connection)CONN), CONN)));
            cs.registerOutParameter(12, 12);
            cs.execute();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + this.errors.toString());
            throw new SiebelBusinessServiceException("SQLException", ex.getMessage());
        }
    }

    public STRUCT[] createStructArray(List<Inventory> inventory, StructDescriptor structDescriptor, Connection ebsConn) throws SQLException {
        STRUCT[] structArray = new STRUCT[inventory.size()];
        Integer index = 0;
        for (Inventory inventories : inventory) {
            STRUCT genericStruct;
            Object[] structObj = new Object[]{inventories.getPart_number(), inventories.getOrg_id(), inventories.getQuantity(), inventories.getAmount(), inventories.getLine_type()};
            structArray[index.intValue()] = genericStruct = new STRUCT(structDescriptor, ebsConn, structObj);
            Integer n = index;
            Integer n2 = index = Integer.valueOf(index + 1);
        }
        return structArray;
    }

    public void createInvoiceQuote(ImplSql method) throws SiebelBusinessServiceException {
        try {
            this.obj = method;
            sqlContext = Context.call(this.obj, true);
            MyLogging.log(Level.INFO, "SQL For InvoiceQuote :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.registerOutParameter(1, 4);
            cs.execute();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + this.errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }

    public void createLocation(ImplSql method) throws SQLException, SiebelBusinessServiceException {
        try {
            this.obj = method;
            sqlContext = Context.call(this.obj);
            MyLogging.log(Level.INFO, "SQL for Location :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.registerOutParameter(1, 4);
            cs.registerOutParameter(2, 12);
            cs.registerOutParameter(3, 4);
            cs.registerOutParameter(4, 12);
            cs.execute();
        }
        catch (SiebelBusinessServiceException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + this.errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }

    public void createPartySite(ImplSql method) throws SiebelBusinessServiceException {
        try {
            this.obj = method;
            sqlContext = Context.call(this.obj);
            MyLogging.log(Level.INFO, "SQL For PartySite :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.registerOutParameter(1, 4);
            cs.registerOutParameter(2, 12);
            cs.registerOutParameter(3, 12);
            cs.registerOutParameter(4, 4);
            cs.registerOutParameter(5, 12);
            cs.execute();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + this.errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }

    public void createSiteUsage(ImplSql method) throws SQLException, SiebelBusinessServiceException {
        try {
            this.obj = method;
            sqlContext = Context.call(this.obj);
            MyLogging.log(Level.INFO, "SQL :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.registerOutParameter(1, 4);
            cs.registerOutParameter(2, 12);
            cs.registerOutParameter(3, 12);
            cs.registerOutParameter(4, 4);
            cs.registerOutParameter(5, 12);
            cs.execute();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + this.errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }

    public void createCustomerSite(ImplSql method) throws SiebelBusinessServiceException {
        try {
            this.obj = method;
            sqlContext = Context.call(this.obj);
            MyLogging.log(Level.INFO, "SQL :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.registerOutParameter(1, 4);
            cs.registerOutParameter(2, 12);
            cs.registerOutParameter(3, 4);
            cs.registerOutParameter(4, 12);
            cs.execute();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + this.errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }

    public void createAccountSite(ImplSql method) throws SiebelBusinessServiceException {
        try {
            this.obj = method;
            sqlContext = Context.call(this.obj);
            MyLogging.log(Level.INFO, "SQL :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.registerOutParameter(1, 4);
            cs.registerOutParameter(2, 12);
            cs.registerOutParameter(3, 12);
            cs.registerOutParameter(4, 4);
            cs.registerOutParameter(5, 12);
            cs.execute();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + this.errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }

    public void createSalesOrder(SiebelDataBean sb, com.plexadasi.order.SalesOrderInventory salesOrder) throws SiebelBusinessServiceException, SiebelException {
        try {
            cs = CONN.prepareCall("{CALL SALES_ORDER_TEST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            cs.setInt(1, DataConverter.toInt(HelperAP.getEbsUserId()));
            cs.setInt(2, DataConverter.toInt(HelperAP.getEbsOrderManagementResp()));
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
            cs.registerOutParameter(15, 1);
            Boolean ex = cs.execute();
            MyLogging.log(Level.INFO, String.valueOf(ex));
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + this.errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }

    public void createOrderReservation(Integer order_number) throws SiebelBusinessServiceException {
        try {
            sqlContext = "{CALL ORDER_MGMT_RESERVATION(?,?,?,?)}";
            MyLogging.log(Level.INFO, "SQL :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.setInt(1, order_number);
            cs.registerOutParameter(2, 12);
            cs.registerOutParameter(3, 12);
            cs.registerOutParameter(4, 2003, "CUSTOMERS_ARRAY");
            cs.execute();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, this.errors.toString());
            throw new SiebelBusinessServiceException("SQL", ex.getMessage());
        }
    }

    public void cancelSalesOrder(String header_id) throws SiebelBusinessServiceException {
        try {
            cs = CONN.prepareCall("{CALL CANCEL_SALES_ORDER(?,?,?,?,?,?,?,?)}");
            cs.setInt(1, DataConverter.toInt(HelperAP.getEbsUserId()));
            cs.setInt(2, DataConverter.toInt(HelperAP.getEbsOrderManagementResp()));
            cs.setString(3, header_id);
            cs.registerOutParameter(4, 12);
            cs.registerOutParameter(5, 4);
            cs.registerOutParameter(6, 12);
            cs.registerOutParameter(7, 12);
            cs.registerOutParameter(8, 2003, "CUSTOMERS_ARRAY");
            cs.execute();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + this.errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }

    public void cancelSalesLineOrder(String header_id, String line_id, String quantity_ordered) throws SiebelBusinessServiceException {
        try {
            cs = CONN.prepareCall("{CALL CANCEL_LINE_ORDER(?,?,?,?,?,?,?,?,?)}");
            cs.setInt(1, DataConverter.toInt(HelperAP.getEbsUserId()));
            cs.setInt(2, DataConverter.toInt(HelperAP.getEbsOrderManagementResp()));
            cs.setInt(3, DataConverter.toInt(header_id));
            cs.setInt(4, DataConverter.toInt(line_id));
            cs.setInt(5, DataConverter.toInt(quantity_ordered));
            cs.registerOutParameter(6, 12);
            cs.registerOutParameter(7, 4);
            cs.registerOutParameter(8, 12);
            cs.registerOutParameter(9, 2003, "CUSTOMERS_ARRAY");
            cs.execute();
            MyLogging.log(Level.SEVERE, header_id + " " + line_id + " " + quantity_ordered);
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + this.errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }

    public void createPurchaseOrder(SiebelDataBean sb, PurchaseOrderInventory poInventory) throws SiebelBusinessServiceException {
        sqlContext = "{CALL PURCHASE_ORDER(?,?,?,?,?,?)}";
        try {
            EBSSqlData ebsData = new EBSSqlData(CONN);
            if (!ebsData.vendorExists(poInventory.getSourceId()).booleanValue()) {
                MyLogging.log(Level.SEVERE, "Please choose a valid supplier.");
                throw new SiebelBusinessServiceException("NOT_FOUND", "Please choose a valid supplier.");
            }
            MyLogging.log(Level.INFO, "SQL :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.setInt(1, poInventory.getSourceId());
            cs.setString(2, poInventory.getShipToLocation());
            cs.setString(3, poInventory.getCurrencyCode());
            cs.setInt(4, poInventory.getAgentCode());
            cs.setString(5, poInventory.getOrderNumber());
            cs.setArray(6, poInventory.inventory(sb, CONN).getInventoryItem());
            MyLogging.log(Level.INFO, poInventory.toString());
            cs.execute();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + this.errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }

    public void CreateItemInEBS(String desc, String part) throws SiebelBusinessServiceException {
        sqlContext = "{CALL CREATE_UPDATE_ITEM(?,?,?,?,?,?,?,?,?)}";
        try {
            MyLogging.log(Level.INFO, "SQL :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.setInt(1, DataConverter.toInt(HelperAP.getEbsUserId()));
            cs.setInt(2, DataConverter.toInt(HelperAP.getEbsInventoryManagerResp()));
            cs.setInt(3, DataConverter.toInt(HelperAP.getEbsAppId()));
            cs.setString(4, part);
            cs.setString(5, desc);
            cs.setString(6, HelperAP.getMasterOrganizationCode());
            cs.setString(7, HelperAP.getTemplateName());
            cs.registerOutParameter(8, 4);
            cs.registerOutParameter(9, 4);
            cs.execute();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + this.errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }

    public void ItemAssignToChild(Integer invId, String orgId) throws SiebelBusinessServiceException {
        sqlContext = "{CALL ITEM_ASSIGN(?,?,?,?,?,?)}";
        try {
            MyLogging.log(Level.INFO, "SQL :" + sqlContext);
            cs = CONN.prepareCall(sqlContext);
            cs.setInt(1, DataConverter.toInt(HelperAP.getEbsUserId()));
            cs.setInt(2, DataConverter.toInt(HelperAP.getEbsInventoryManagerResp()));
            cs.setInt(3, DataConverter.toInt(HelperAP.getEbsAppId()));
            cs.setInt(4, invId);
            cs.setString(5, orgId);
            cs.registerOutParameter(6, 2005);
            cs.execute();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + this.errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }

    public String getString(int value) throws SQLException {
        return cs.getString(value);
    }

    public Integer getInt(int value) throws SQLException {
        return cs.getInt(value);
    }

    public Date getDate(String value) throws SQLException {
        return cs.getDate(value);
    }

    public long getLong(int value) throws SQLException {
        return cs.getLong(value);
    }

    public Array getArray(int value) throws SQLException {
        return cs.getArray(value);
    }
}

