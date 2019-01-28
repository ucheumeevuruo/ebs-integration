/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.plexadasi.connect.ebs.EbsConnect
 *  com.plexadasi.connect.siebel.SiebelConnect
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelException
 *  com.siebel.eai.SiebelBusinessServiceException
 *  org.apache.commons.dbutils.OutParameter
 *  org.apache.commons.dbutils.QueryRunner
 *  org.apache.commons.dbutils.ResultSetHandler
 *  org.apache.commons.dbutils.handlers.BeanHandler
 *  org.apache.commons.dbutils.handlers.BeanListHandler
 */
package com.plexadasi.ebs.services;

import com.plexadasi.helper.DataConverter;
import com.plexadasi.helper.HelperAP;
import com.plexadasi.build.SqlPreparedStatement;
import com.plexadasi.connect.ebs.EbsConnect;
import com.plexadasi.connect.siebel.SiebelConnect;
import com.plexadasi.ebs.Inventory;
import com.plexadasi.ebs.SiebelApplication.bin.SOInventory;
import com.plexadasi.ebs.SiebelApplication.bin.SalesOrderInventory;
import com.plexadasi.ebs.CallableRunner;
import com.plexadasi.ebs.model.BackOrder;
import com.plexadasi.ebs.model.Order;
import com.plexadasi.ebs.services.sql.build.SQLBuilder;
import com.plexadasi.siebel.Iinventory;
import com.plexadasi.siebel.model.OrderEntry;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.naming.NamingException;
import org.apache.commons.dbutils.OutParameter;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class SalesOrderService {
    private final QueryRunner jdbcTemplateObject;
    private final SQLBuilder sqlBuilder = new SQLBuilder();
    private String query;
    private final Connection connection;

    public SalesOrderService(Connection connection) {
        this.connection = connection;
        this.jdbcTemplateObject = new QueryRunner();
    }

    public Map<String, Object> createSalesOrder(com.plexadasi.order.SalesOrderInventory salesOrder) throws SQLException, SiebelBusinessServiceException, SiebelException {
        CallableRunner prun = new CallableRunner();
        Map<String, Object> order = prun.queryProc(this.connection, 
                "{CALL SALES_ORDER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", 
            new Object[]{DataConverter.toInt(HelperAP.getEbsUserId()), 
                DataConverter.toInt(HelperAP.getEbsOrderManagementResp()), 
                DataConverter.toInt(HelperAP.getOrderTypeId()), 
                salesOrder.getSoldToOrgId(), 
                salesOrder.getShipToId(), 
                salesOrder.getBillToId(), 
                salesOrder.getSoldFromId(), 
                salesOrder.getSalesRepId(), 
                DataConverter.toInt(HelperAP.getPriceListID()), 
                salesOrder.getTransactionCode(), 
                salesOrder.getStatusCode(), 
                salesOrder.getSiebelOrderId(), 
                salesOrder.getSourceId(), 
                salesOrder.inventoryItems(SiebelConnect.connectSiebelServer(), this.connection).getInventoryItem(), 
                new OutParameter(Types.VARCHAR, String.class, "1"), 
                new OutParameter(Types.VARCHAR, String.class, "2"), 
                new OutParameter(Types.INTEGER, Integer.class, 3)
            });
        return order;
    }

    public Map<String, Object> createInvoice(OrderEntry salesOrder, List<Iinventory> inventories) throws SQLException {
        Inventory inventory = new Inventory(this.connection, inventories);
        CallableRunner prun = new CallableRunner();
        Map<String, Object> order = prun.queryProc(this.connection, "{CALL CREATE_SINGLE_INVOICE(?,?,?,?,?,?,?,?,?,?,?,?,?)}", new Object[]{DataConverter.toInt(HelperAP.getEbsUserId()), DataConverter.toInt(HelperAP.getEbsReceivableManagerResp()), DataConverter.toInt(HelperAP.getEbsAppId()), DataConverter.toInt(HelperAP.getSourceBatchId()), salesOrder.getBillingId(), salesOrder.getTransactionType(), salesOrder.getCurrencyCode(), HelperAP.getOrderTypeId(), DataConverter.toInt(HelperAP.getLegalEntity()), salesOrder.getSalesPerson(), inventory.getInventoryItems(), new OutParameter(4, Integer.class), new OutParameter(4, Integer.class)});
        return order;
    }

    public Map<String, Object> returnSalesOrder(com.plexadasi.order.SalesOrderInventory salesOrder) throws SQLException, SiebelBusinessServiceException, SiebelException {
        CallableRunner prun = new CallableRunner();
        Map<String, Object> order = prun.queryProc(this.connection, "{CALL SALES_ORDER_RETURN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", 
                new Object[]{
                    DataConverter.toInt(HelperAP.getEbsUserId()), 
                    DataConverter.toInt(HelperAP.getEbsOrderManagementResp()), 
                    DataConverter.toInt(HelperAP.getOrderTypeId()), 
                    salesOrder.getSoldToOrgId(), 
                    salesOrder.getShipToId(), 
                    salesOrder.getBillToId(), 
                    salesOrder.getSoldFromId(), 
                    salesOrder.getSalesRepId(), 
                    DataConverter.toInt(HelperAP.getPriceListID()), 
                    salesOrder.getTransactionCode(), 
                    salesOrder.getStatusCode(), 
                    salesOrder.getSiebelOrderId(), 
                    salesOrder.getSourceId(), 
                    salesOrder.inventoryItems(SiebelConnect.connectSiebelServer(), this.connection).getInventoryItem(), 
                    new OutParameter(12, Integer.class), 
                    new OutParameter(12, Integer.class), 
                    new OutParameter(4, Integer.class)
                });
        return order;
    }

    public Integer updateInvoice(Integer transactionId, OrderEntry orderEntry) throws SQLException {
        this.query = "UPDATE RA_CUSTOMER_TRX_ALL SET CT_REFERENCE = ?, ATTRIBUTE1 = ?, ATTRIBUTE2 = ? WHERE CUSTOMER_TRX_ID = ?";
        return this.jdbcTemplateObject.update(this.connection, this.query, new Object[]{orderEntry.getReferenceNumber(), orderEntry.getInvoiceNumber(), orderEntry.getPaymentMode(), transactionId});
    }

    public void updateSalesOrder(com.plexadasi.order.SalesOrderInventory salesOrder) throws SQLException, SiebelBusinessServiceException, SiebelException {
        CallableRunner prun = new CallableRunner();
        this.query = "{CALL UPDATE_SALES_ORDER(?,?,?,?)}";
        prun.queryProc(this.connection, this.query, HelperAP.getEbsUserId(), HelperAP.getEbsOrderManagementResp(), HelperAP.getEbsAppId(), salesOrder.inventory(SiebelConnect.connectSiebelServer(), this.connection).getInventoryItem());
    }

    public void createReservation(Integer orderId) throws SQLException {
        CallableRunner prun = new CallableRunner();
        this.query = "{CALL ORDER_MGMT_RESERVATION(?,?,?,?)}";
        prun.queryProc(this.connection, this.query, HelperAP.getEbsUserId(), HelperAP.getEbsOrderManagementResp(), HelperAP.getEbsAppId(), orderId);
    }

    public Map<String, Object> cancelSalesOrder(Integer headerId, Integer lineId) 
        throws SQLException {
        CallableRunner prun = new CallableRunner();
        this.query = "{CALL CANCEL_SALES_ORDER(?,?,?,?,?)}";
        return prun.queryProc(
            this.connection,
            this.query, 
            new Object[]{
                HelperAP.getEbsUserId(), 
                HelperAP.getEbsOrderManagementResp(), 
                HelperAP.getEbsAppId(), 
                headerId,
                new OutParameter(12, String.class)
            }
        );
    }

    public void cancelSalesOrderLineItem(Integer headerId, Integer lineId, Integer quantity) throws SQLException {
        CallableRunner prun = new CallableRunner();
        this.query = "{CALL CANCEL_LINE_ORDER(?,?,?,?,?,?,?,?,?)}";
        prun.queryProc(
            this.connection, 
            this.query, 
            HelperAP.getEbsUserId(), 
            HelperAP.getEbsOrderManagementResp(), 
            HelperAP.getEbsAppId(), 
            headerId, 
            lineId, 
            quantity
        );
    }

    public BackOrder findBookingLineItemStatus(Order salesOrder) throws SQLException {
        BeanHandler rowMapper = new BeanHandler(BackOrder.class);
        this.query = this.sqlBuilder.buildBackOrderSql().getQuery();
        BackOrder backOrder = 
            (BackOrder)this.jdbcTemplateObject.query(
                this.connection, 
                this.query, 
                (ResultSetHandler)rowMapper, 
                new Object[]{
                    salesOrder.getOrderNumber(), 
                    salesOrder.getBackOfficeNumber(),
                    salesOrder.getPartNumber(), 
                    salesOrder.getWarehouseId()
                }
        );
        return backOrder;
    }

    public Order findOnHandQuantity(Order salesOrder, String type) throws SQLException {
        BeanHandler rowMapper = new BeanHandler(Order.class);
        this.query = "SELECT XXMADN_GET_OHQTY(SEGMENT1, ORGANIZATION_ID, ?) QUANTITY FROM MTL_SYSTEM_ITEMS_B ";
        this.query += "WHERE SEGMENT1=? AND ORGANIZATION_ID=?";
        Order onhand = (Order)this.jdbcTemplateObject.query(this.connection, this.query, (ResultSetHandler)rowMapper, new Object[]{type, salesOrder.getPartNumber(), salesOrder.getWarehouseId()});
        return onhand != null ? onhand : new Order();
    }

    public List<Order> findOnHandQuantity(String partNumber, String location1, String location2) throws SQLException {
        BeanListHandler rowMapper = new BeanListHandler(Order.class);
        this.query = this.sqlBuilder.buildOnhandQuantitySql().getQuery();
        List onhand = (List)this.jdbcTemplateObject.query(this.connection, this.query, (ResultSetHandler)rowMapper, 
            new Object[]{
                partNumber, 
                location1, 
                location2
            }
        );
        return onhand != null ? onhand : new ArrayList();
    }

    public static void main(String[] args) throws SiebelBusinessServiceException, SQLException, SiebelException, NamingException {
        Connection ebs = EbsConnect.connectToEBSDatabase();
        SalesOrderService sos = new SalesOrderService(ebs);
        BillingAccountService billing = new BillingAccountService(ebs);
        com.plexadasi.order.SalesOrderInventory s = new com.plexadasi.order.SalesOrderInventory();
        int ebs_id = 51086;
        s.setSiebelOrderId("1-34666031");
        s.setOrderId(1001);
        s.setSoldToOrgId(ebs_id);
        s.setBillToId(billing.findBillToCode(ebs_id));
        s.setShipToId(billing.findShipToCode(ebs_id));
        s.setSoldFromId(123);
        s.setSalesRepId(100000040);
        s.setTransactionCode("NGN");
        s.setStatusCode("ENTERED");
        s.setPurchaseOrderNumber("1-30474202");
        s.setSourceId(0);
        Map<String, Object> map = sos.createSalesOrder(s);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " +entry.getValue());
        }
    }
}

