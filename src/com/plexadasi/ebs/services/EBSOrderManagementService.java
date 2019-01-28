/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelException
 *  com.siebel.eai.SiebelBusinessServiceException
 *  org.apache.commons.dbutils.OutParameter
 *  org.apache.commons.dbutils.QueryRunner
 */
package com.plexadasi.ebs.services;

import com.plexadasi.helper.DataConverter;
import com.plexadasi.helper.HelperAP;
import com.plexadasi.ebs.Inventory;
import com.plexadasi.ebs.PurchaseInventory;
import com.plexadasi.ebs.CallableRunner;
import com.plexadasi.ebs.SalesOrderInventory;
import com.plexadasi.siebel.Iinventory;
import com.plexadasi.siebel.model.OrderEntry;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbutils.OutParameter;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

public final class EBSOrderManagementService {
    private final QueryRunner jdbcTemplateObject;
    private final Connection connection;
    private String query;
    private final OrderEntry salesOrder;
    private final List<Iinventory> inventories;

    public EBSOrderManagementService(Connection connection, OrderEntry salesOrder, List<Iinventory> inventories) {
        this.connection = connection;
        this.salesOrder = salesOrder;
        this.inventories = inventories;
        this.jdbcTemplateObject = new QueryRunner();
    }

    /**
     * 
     * @return
     * @throws SQLException
     * @throws SiebelBusinessServiceException
     * @throws SiebelException 
     */
    public Map<String, Object> createOrder() 
        throws SQLException, SiebelBusinessServiceException, SiebelException {
        CallableRunner prun = new CallableRunner();
        BillingAccountService bas = new BillingAccountService(this.connection);
        SalesOrderInventory inventory = new SalesOrderInventory(this.connection, this.inventories);
        return prun.queryProc(
            this.connection, 
            "{CALL SALES_ORDER_TESTS(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", 
            new Object[]{
                DataConverter.toInt(HelperAP.getEbsUserId()), 
                DataConverter.toInt(HelperAP.getEbsOrderManagementResp()), 
                //DataConverter.toInt(HelperAP.getOrderTypeId()), 
                this.salesOrder.getIntegrationId(), 
                bas.findShipToCode(this.salesOrder.getIntegrationId()),
                bas.findBillToCode(this.salesOrder.getIntegrationId()),
                this.salesOrder.getWarehouseId(), 
                this.salesOrder.getSalesPerson(), 
                this.salesOrder.getCurrencyCode(),
                this.salesOrder.getOrderId(),
                this.salesOrder.getOrderNumber(), 
                inventory.getInventoryItems(), 
                new OutParameter(12, Integer.class, "FLOW_STATUS_CODE"), 
                new OutParameter(12, Integer.class, "ORDER_NUMBER"),
                new OutParameter(12, Integer.class, "HEADER_ID")
            }
        );
    }

    /**
     * 
     * @return
     * @throws SQLException 
     */
    public Map<String, Object> createInvoice() throws SQLException {
        CallableRunner prun = new CallableRunner();
        Inventory inventory = new Inventory(this.connection, this.inventories);
        return prun.queryProc(
            this.connection, 
            "{CALL CREATE_SINGLE_INVOICE(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", 
            new Object[]{
                DataConverter.toInt(HelperAP.getEbsUserId()), 
                DataConverter.toInt(HelperAP.getEbsReceivableManagerResp()), 
                DataConverter.toInt(HelperAP.getEbsAppId()), 
                DataConverter.toInt(HelperAP.getSourceBatchId()), 
                this.salesOrder.getBillingId(), 
                this.salesOrder.getTransactionType(), 
                this.salesOrder.getCurrencyCode(), 
                HelperAP.getOrderTypeId(), 
                DataConverter.toInt(HelperAP.getLegalEntity()), 
                this.salesOrder.getSalesPerson(), 
                this.salesOrder.getOrderNumber(),
                inventory.getInventoryItems(), 
                new OutParameter(Types.VARCHAR, String.class, "CUST_TRX_ID"), 
                new OutParameter(Types.VARCHAR, String.class, "TRX_NUMBER")
            }
        );
    }

    /**
     * 
     * @return
     * @throws SQLException
     * @throws SiebelBusinessServiceException
     * @throws SiebelException 
     */
    public Map<String, Object> returnOrder() 
            throws SQLException, SiebelBusinessServiceException, SiebelException {
        CallableRunner prun = new CallableRunner();
        Inventory inventory = new Inventory(this.connection, this.inventories);
        this.query = "{CALL SALES_ORDER_RETURN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        return prun.queryProc(
            this.connection, 
            this.query, 
            new Object[]{
                DataConverter.toInt(HelperAP.getEbsUserId()), 
                DataConverter.toInt(HelperAP.getEbsOrderManagementResp()), 
                DataConverter.toInt(HelperAP.getOrderTypeId()), 
                inventory.getInventoryItems(), 
                new OutParameter(12, Integer.class), 
                new OutParameter(12, Integer.class), 
                new OutParameter(4, Integer.class)
            }
        );
        
    }

    /**
     * 
     * @param transactionId
     * @return
     * @throws SQLException 
     */
    public Integer updateInvoice(Integer transactionId) throws SQLException {
        this.query = "UPDATE RA_CUSTOMER_TRX_ALL SET CT_REFERENCE = ?, ATTRIBUTE1 = ?, ATTRIBUTE2 = ? WHERE CUSTOMER_TRX_ID = ?";
        return this.jdbcTemplateObject.update(
            this.connection, 
            this.query, 
            new Object[]{
                this.salesOrder.getReferenceNumber(), 
                this.salesOrder.getInvoiceNumber(), 
                this.salesOrder.getPaymentMode(), 
                transactionId
            }
        );
    }

    /**
     * 
     * @return
     * @throws SQLException 
     */
    public Map<String, Object> getInventoryId() throws SQLException{
        Object[] params = new Object[]{this.salesOrder.getOrderNumber()};
        this.query = "SELECT HEADER_ID, ORIG_SYS_DOCUMENT_REF as INTEGRATION_ID, ORDER_NUMBER, CUST_PO_NUMBER FROM OE_ORDER_HEADERS_ALL "
                + "WHERE CUST_PO_NUMBER = ?";
        
        if(this.salesOrder.getBackOfficeOrderNumber() != null){
            this.query += " AND ORDER_NUMBER = ?";
            params = new Object[]{this.salesOrder.getOrderNumber(),this.salesOrder.getBackOfficeOrderNumber()};
        }System.out.println(params.length);
        ResultSetHandler<Map<String, Object>> rsh =
                new MapHandler();
        
        return this.jdbcTemplateObject.query(
            this.connection, 
            this.query,
            rsh,
            params
        );
    }
    
    /**
     * 
     * @throws SQLException 
     */
    public void createReservation() throws SQLException {
        CallableRunner prun = new CallableRunner();
        this.query = "{CALL ORDER_MGMT_RESERVATION(?,?,?,?)}";
        prun.queryProc(
            this.connection, 
            this.query, 
            HelperAP.getEbsUserId(), 
            HelperAP.getEbsOrderManagementResp(), 
            HelperAP.getEbsAppId(), 
            this.salesOrder.getOrderNumber()
        );
    }

    /**
     * 
     * @return
     * @throws SQLException 
     */
    public Map<String, Object> cancelOrder() throws SQLException {
        CallableRunner prun = new CallableRunner();
        this.query = "{CALL CANCEL_SALES_ORDER(?,?,?,?,?)}";
        return prun.queryProc(
            this.connection, 
            this.query, 
            new Object[]{
                HelperAP.getEbsUserId(), 
                HelperAP.getEbsOrderManagementResp(), 
                HelperAP.getEbsAppId(), 
                this.salesOrder.getOrderNumber(), 
                new OutParameter(12, String.class)
            }
        );
    }

    /**
     * 
     * @throws SQLException 
     */
    public void cancelOrderLineItems() throws SQLException {
        CallableRunner prun = new CallableRunner();
        Inventory inventory = new Inventory(this.connection, this.inventories);
        this.query = "{CALL CANCEL_LINE_ORDER_TEST(?,?,?,?,?,?)}";
        prun.queryProc(
            this.connection, 
            this.query, 
            new Object[]{
                HelperAP.getEbsUserId(), 
                HelperAP.getEbsOrderManagementResp(), 
                HelperAP.getEbsAppId(), 
                this.salesOrder.getOrderNumber(), 
                this.salesOrder.getBackOfficeOrderNumber(),
                inventory.getInventoryItems()
            }
        );
    }

    /**
     * 
     * @return
     * @throws SQLException 
     */
    public Object checkATP() throws SQLException {
        ResultSetHandler<String> rsh = new ScalarHandler<String>();
        Inventory inventory = new Inventory(this.connection, this.inventories, "ATP", "PRODUCTS");
        this.query = "select XXMADN_CHECK_ATP_TEST(?) from dual";
        return jdbcTemplateObject.query(
            this.connection, 
            query, 
            rsh, 
            new Object[]{
                this.salesOrder.getSiebelMessage()
            }
        );
    }

    public Map<String, Object> createPurchaseOrder() throws SQLException {
        CallableRunner prun = new CallableRunner();
        PurchaseInventory inventory = new PurchaseInventory(this.connection, this.inventories);
        this.query = "{CALL PURCHASE_ORDER(?,?,?,?,?,?,?,?)}";
        return prun.queryProc(
            this.connection, 
            this.query, 
            new Object[]{ 
                this.salesOrder.getVendorId(), 
                this.salesOrder.getWarehouseId(),
                this.salesOrder.getCurrencyCode(), 
                this.salesOrder.getAgentId(), 
                this.salesOrder.getOrderNumber(), 
                this.salesOrder.getShippingMethod(),
                inventory.getInventoryItems()
                ,new OutParameter(12, String.class, "STATUS")
            }
        );
    }
}

