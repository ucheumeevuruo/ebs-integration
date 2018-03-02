/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.order;

import com.plexadasi.Helper.DataConverter;
import com.plexadasi.build.EBSSql;
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.model.BackOrder;
import com.plexadasi.ebs.model.BillingAccount;
import com.plexadasi.ebs.services.BillingAccountService;
import com.plexadasi.ebs.services.SalesOrderService;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
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
import java.util.logging.Logger;

/**
 *
 * @author SAP Training
 */
public class SalesOrder {
    private Connection connection;
    private SiebelDataBean siebelConnection = new SiebelDataBean();
    private static final StringWriter ERROR = new StringWriter();
    private final List<String> hList = new ArrayList();
    private String returnValue = "";
    private String statusCode = "";
    private Integer orderNumber = 0;
    private EBSSql ebs = null;
    private String line_order_status_code;
    
    public SalesOrder(){
        MyLogging.log(Level.INFO, "===============================");
        MyLogging.log(Level.INFO, "=="+getClass().getSimpleName());
        MyLogging.log(Level.INFO, "===============================");
    }
    
    public void doInvoke(SalesOrderInventory salesOrder, SiebelDataBean sb, Connection ebs) throws SiebelBusinessServiceException
    {
        try
        { 
            siebelConnection = sb;
            connection = ebs;
        }catch(NullPointerException ex){
            MyLogging.log(Level.SEVERE, "Connection could not be established.");
            throw new SiebelBusinessServiceException("CONN_ERROR", "Connection could not be established.");
        }
        this.generateSalesOrder(salesOrder);
    }
    
    public String getReturnStatus()
    {
        return returnValue;
    }
    
    public String getFlowStatusCode()
    {
        return statusCode;
    }
    
    public List<String> getReturnMessages()
    {
        return hList;
    }
    
    public Integer getOrderNumber()
    {
        return orderNumber;
    }
    
    public String getSalesOrderBookingStatus(Connection ebsConn, String order_num) throws SiebelBusinessServiceException
    {
        return new EBSSqlData(ebsConn).getOrderBookingStatus(
            "FLOW_STATUS_CODE", 
            "OE_ORDER_HEADERS_ALL", 
            "ORDER_NUMBER", 
            order_num
        );
    }
    
    public String getOrderLineBookngStatus(){
        return this.line_order_status_code;
    }
    
    /*
        @SalesOrderInventory
    */
    private void generateSalesOrder(SalesOrderInventory salesOrder) throws SiebelBusinessServiceException
    {
        SalesOrderService sos = new SalesOrderService(this.connection);
        
        try 
        {
            Integer orgId = salesOrder.getSoldToOrgId();
            BillingAccountService bas = new BillingAccountService(this.connection);
            salesOrder.setShipToId(bas.findShipToCode(orgId));
            salesOrder.setBillToId(bas.findBillToCode(orgId));
            
            // Lets log the values from the sales order inventory class.
            // This will aid in debugging any issue that might arise in the future
            List<SalesOrderInventory> list = new ArrayList();
            list.add(salesOrder);
            MyLogging.log(Level.INFO, "Describe Sales Order Inventory Object \n" + list);
            
            // Create the the sales order in siebel.
            Map<Integer, Object> item = sos.createSalesOrder(salesOrder);
            this.returnValue = (String)item.get(15);
            this.statusCode = (String)item.get(16);
            this.orderNumber = (Integer)item.get(17);
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "SQL:" + ERROR.toString());
            throw new SiebelBusinessServiceException("SQL", ex.getMessage());
        } catch (SiebelException ex) {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "SIEBEL:" + ERROR.toString());
        }
    }
    
    public void generateOrderReservation() throws SiebelBusinessServiceException
    {
        try {
            SalesOrderService salesOrderService = new SalesOrderService(this.connection);
            salesOrderService.createReservation(this.orderNumber);
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "SQL:" + ERROR.toString());
            throw new SiebelBusinessServiceException("SQL", "Cannot create reservation."
                    + " Please try again later.");
        }
    }
    
    public BackOrder getSalesOrderLineItemStatus(Connection connection, com.plexadasi.ebs.model.Order salesOrder) throws SiebelBusinessServiceException{
        try {
            this.connection = connection;
            SalesOrderService salesOrderService = new SalesOrderService(this.connection);
            return salesOrderService.findBookingLineItemStatus(salesOrder);
        } catch(SQLException ex) {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, ERROR.toString());
        }
        return new BackOrder();
    }
    
    public String onHandStatus(Connection ebsConn, Integer order_number, Integer inventory_id) throws SiebelBusinessServiceException, SQLException{
        EBSSqlData ebsData = new EBSSqlData(ebsConn);
        String[] ret = ebsData.getHeaderLineId(order_number, inventory_id);
        if(ret.length == 0){
            throw new SiebelBusinessServiceException("NUM_EXCEPT", "Inventory does not exists. Please check your order number and try again.");
        }
        String line_id;
        line_id = ret[0];
        return new EBSSqlData(ebsConn).getOrderBookingStatus(
            "FLOW_STATUS_CODE", 
            "OE_ORDER_LINES_ALL", 
            "LINE_ID", 
            line_id
        );
    }
    
    public void cancelOrder(Connection ebsConn, Integer order_number) throws SiebelBusinessServiceException, SQLException{
        this.connection = ebsConn;
        SalesOrderService sos = new SalesOrderService(ebsConn);
        EBSSqlData ebsData = new EBSSqlData(ebsConn);
        String ret = ebsData.getHeaderId(order_number);
        MyLogging.log(Level.SEVERE, "Order Number:" + ret);
        if(ret.length() == 0){
            throw new SiebelBusinessServiceException("NUM_EXCEPT", "Invalid order number. Please check your order number and try again.");
        }
        
        //ebs = new EBSSql(ebsConn);
        //ebs.cancelSalesOrder(ret);
        Map<Integer, Object> cso = sos.cancelSalesOrder(DataConverter.toInt(ret), orderNumber);
        this.statusCode = (String)cso.get(5);
        /*
        returnValue = ebs.getString(4);
        this.statusCode = ebs.getString(7);
        Array arr = ebs.getArray(8);
        String[] data = new String[]{};
        if (arr != null) 
        {
           data = (String[]) arr.getArray();
        }
        MyLogging.log(Level.INFO, "----------------------------Return message--------------------------");
        for (String data1 : data) 
        {
            hList.add(data1);
            MyLogging.log(Level.INFO, data1);
        }*/
    }
    
    public void cancelLineOrder(Connection ebsConn, Integer order_number, Integer inventory_id) throws SiebelBusinessServiceException, SQLException{
        EBSSqlData ebsData = new EBSSqlData(ebsConn);
        String[] ret = ebsData.getHeaderLineId(order_number, inventory_id);
        if(ret.length == 0){
            throw new SiebelBusinessServiceException("NUM_EXCEPT", "Inventory does not exists. Please check your order number and try again.");
        }
        String header, line_id, quantity_ordered;
        line_id = ret[0];
        header = ret[1];
        quantity_ordered = ret[2];
        ebs = new EBSSql(ebsConn);
        ebs.cancelSalesLineOrder(header, line_id, quantity_ordered);
        this.returnValue = ebs.getString(6);
        //this.statusCode = ebs.getString(7);
        Array arr = ebs.getArray(9);
        this.line_order_status_code = this.onHandStatus(ebsConn, order_number, inventory_id);
        String[] data = new String[]{};
        if (arr != null) 
        {
           data = (String[]) arr.getArray();
        }
        MyLogging.log(Level.INFO, "########################### PROCESS INFO #############################");
        if(!this.returnValue.equalsIgnoreCase("s")){
            MyLogging.log(Level.INFO, "----------------------------Return message--------------------------");
            for (String data1 : data) 
            {
                hList.add(data1);
                MyLogging.log(Level.INFO, data1);
            }
        }
    }
}
