/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelException
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.order;

import com.plexadasi.helper.DataConverter;
import com.plexadasi.build.EBSSql;
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.model.BackOrder;
import com.plexadasi.ebs.model.Order;
import com.plexadasi.ebs.services.BillingAccountService;
import com.plexadasi.ebs.services.SalesOrderService;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class SalesOrder {
    private Connection connection;
    private SiebelDataBean siebelConnection = new SiebelDataBean();
    private static final StringWriter ERROR = new StringWriter();
    private final List<String> hList = new ArrayList<String>();
    private String returnValue = "";
    private String statusCode = "";
    private Integer orderNumber = 0;
    private EBSSql ebs = null;
    private String line_order_status_code = "";

    public SalesOrder() {
    }

    public SalesOrder(SiebelDataBean siebelConnection, Connection connection) throws SiebelBusinessServiceException {
        this.siebelConnection = siebelConnection;
        this.connection = connection;
    }

    public void doInvoke(SalesOrderInventory salesOrder, SiebelDataBean siebelConnection, Connection connection) throws SiebelBusinessServiceException {
        String type;
        this.siebelConnection = siebelConnection;
        this.connection = connection;
        type = salesOrder.getType() != null ? salesOrder.getType() : "";
        if (type.equals("return_order")) {
            this.returnSalesOrder(salesOrder);
        } else {
            this.generateSalesOrder(salesOrder);
        }
    }

    public String getReturnStatus() {
        return this.returnValue;
    }

    public String getFlowStatusCode() {
        return this.statusCode;
    }

    public List<String> getReturnMessages() {
        return this.hList;
    }

    public Integer getOrderNumber() {
        return this.orderNumber;
    }

    public String getSalesOrderBookingStatus(Connection ebsConn, String order_num) throws SiebelBusinessServiceException {
        return new EBSSqlData(ebsConn).getOrderBookingStatus("FLOW_STATUS_CODE", "OE_ORDER_HEADERS_ALL", "ORDER_NUMBER", order_num);
    }

    public String getOrderLineBookngStatus() {
        return this.line_order_status_code;
    }

    private void generateSalesOrder(SalesOrderInventory salesOrder) throws SiebelBusinessServiceException {
        SalesOrderService sos = new SalesOrderService(this.connection);
        try {
            Integer orgId = salesOrder.getSoldToOrgId();
            salesOrder.setShipToId(0);
            salesOrder.setBillToId(0);
            try {
                BillingAccountService bas = new BillingAccountService(this.connection);
                salesOrder.setShipToId(bas.findShipToCode(orgId));
                salesOrder.setBillToId(bas.findBillToCode(orgId));
            }
            
            // 
            catch (SQLException ex) {
                ex.printStackTrace(new PrintWriter(ERROR));
                MyLogging.log(Level.SEVERE, "SQL:" + ERROR.toString());
            }
            ArrayList<SalesOrderInventory> list = new ArrayList<SalesOrderInventory>();
            list.add(salesOrder);
            MyLogging.log(Level.INFO, "Describe Sales Order Inventory Object \n" + list);
            Map<String, Object> item = sos.createSalesOrder(salesOrder);
            this.returnValue = (String)item.get("1");
            this.statusCode = (String)item.get("2");
            this.orderNumber = DataConverter.toInt(String.valueOf(item.get("3")));
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "SQL:" + ERROR.toString());
            throw new SiebelBusinessServiceException("SQL", ex.getMessage());
        }
        catch (SiebelException ex) {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "SIEBEL:" + ERROR.toString());
        }
    }

    public void returnSalesOrder(SalesOrderInventory salesOrder) throws SiebelBusinessServiceException {
        SalesOrderService sos = new SalesOrderService(this.connection);
        try {
            Integer orgId = salesOrder.getSoldToOrgId();
            salesOrder.setShipToId(0);
            salesOrder.setBillToId(0);
            try {
                BillingAccountService bas = new BillingAccountService(this.connection);
                salesOrder.setShipToId(bas.findShipToCode(orgId));
                salesOrder.setBillToId(bas.findBillToCode(orgId));
            }
            catch (SQLException ex) {
                ex.printStackTrace(new PrintWriter(ERROR));
                MyLogging.log(Level.SEVERE, "SQL:" + ERROR.toString());
            }
            ArrayList<SalesOrderInventory> list = new ArrayList<SalesOrderInventory>();
            list.add(salesOrder);
            MyLogging.log(Level.INFO, "Describe Sales Order Inventory Object \n" + list);
            Map<String, Object> item = sos.returnSalesOrder(salesOrder);
            this.returnValue = (String)item.get(15);
            this.statusCode = (String)item.get(16);
            this.orderNumber = (Integer)item.get(17);
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "SQL:" + ERROR.toString());
        }
        catch (SiebelException ex) {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "SIEBEL:" + ERROR.toString());
        }
    }

    public void generateOrderReservation() throws SiebelBusinessServiceException {
        try {
            SalesOrderService salesOrderService = new SalesOrderService(this.connection);
            salesOrderService.createReservation(this.orderNumber);
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "SQL:" + ERROR.toString());
            throw new SiebelBusinessServiceException("SQL", "Cannot create reservation. Please try again later.");
        }
    }

    public BackOrder getSalesOrderLineItemStatus(Connection connection, Order salesOrder) throws SiebelBusinessServiceException {
        try {
            this.connection = connection;
            SalesOrderService salesOrderService = new SalesOrderService(this.connection);
            return salesOrderService.findBookingLineItemStatus(salesOrder);
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, ERROR.toString());
            return new BackOrder();
        }
    }

    public String onHandStatus(Connection ebsConn, Integer order_number, Integer inventory_id) throws SiebelBusinessServiceException, SQLException {
        EBSSqlData ebsData = new EBSSqlData(ebsConn);
        String[] ret = ebsData.getHeaderLineId(order_number, inventory_id);
        if (ret.length == 0) {
            throw new SiebelBusinessServiceException("NUM_EXCEPT", "Inventory does not exists. Please check your order number and try again.");
        }
        String line_id = ret[0];
        return new EBSSqlData(ebsConn).getOrderBookingStatus("FLOW_STATUS_CODE", "OE_ORDER_LINES_ALL", "LINE_ID", line_id);
    }

    public void cancelOrder(Connection ebsConn, Integer order_number) throws SiebelBusinessServiceException, SQLException {
        this.connection = ebsConn;
        SalesOrderService sos = new SalesOrderService(ebsConn);
        EBSSqlData ebsData = new EBSSqlData(ebsConn);
        String ret = ebsData.getHeaderId(order_number);
        MyLogging.log(Level.INFO, "Order Number:" + ret);
        if (ret.length() == 0) {
            throw new SiebelBusinessServiceException("NUM_EXCEPT", "Invalid order number. Please check your order number and try again.");
        }
        Map<String, Object> cso = sos.cancelSalesOrder(DataConverter.toInt(ret), this.orderNumber);
        this.statusCode = (String)cso.get(5);
    }

    public void cancelLineOrder(Connection ebsConn, Integer order_number, Integer inventory_id) throws SiebelBusinessServiceException, SQLException {
        EBSSqlData ebsData = new EBSSqlData(ebsConn);
        String[] ret = ebsData.getHeaderLineId(order_number, inventory_id);
        if (ret.length == 0) {
            throw new SiebelBusinessServiceException("NUM_EXCEPT", "Inventory does not exists. Please check your order number and try again.");
        }
        String line_id = ret[0];
        String header = ret[1];
        String quantity_ordered = ret[2];
        this.ebs = new EBSSql(ebsConn);
        this.ebs.cancelSalesLineOrder(header, line_id, quantity_ordered);
        this.returnValue = this.ebs.getString(6);
        Array arr = this.ebs.getArray(9);
        this.line_order_status_code = this.onHandStatus(ebsConn, order_number, inventory_id);
        String[] data = new String[]{};
        if (arr != null) {
            data = (String[])arr.getArray();
        }
        MyLogging.log(Level.INFO, "########################### PROCESS INFO #############################");
        if (!this.returnValue.equalsIgnoreCase("s")) {
            MyLogging.log(Level.INFO, "----------------------------Return message--------------------------");
            for (String data1 : data) {
                this.hList.add(data1);
                MyLogging.log(Level.INFO, data1);
            }
        }
    }
}

