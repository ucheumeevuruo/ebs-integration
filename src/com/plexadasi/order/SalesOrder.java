/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.order;

import com.plexadasi.build.EBSSql;
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.Helper.DataConverter;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.siebel.data.SiebelDataBean;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author SAP Training
 */
public class SalesOrder {
    private Connection EBS_CONN = null;
    private SiebelDataBean SIEBEL_CONN = new SiebelDataBean();
    private static final StringWriter ERROR = new StringWriter();
    private Integer integerOutput;
    private String stringOutput;
    private final List<String> hList = new ArrayList();
    private String returnValue = "";
    private String statusCode;
    private String orderNumber;
    private EBSSql e = null;
    private String line_order_status_code;
    
    public void doInvoke(SalesOrderInventory salesOrder, SiebelDataBean sb, Connection ebs) throws SiebelBusinessServiceException
    {
        SIEBEL_CONN = sb;
        EBS_CONN = ebs;
        if(SIEBEL_CONN == null)
        {
            MyLogging.log(Level.SEVERE, "Connection to siebel cannot be established.");
            throw new SiebelBusinessServiceException("NULL_DEF", "Connection to siebel cannot be established.");
        }
        else if(EBS_CONN == null)
        {
            MyLogging.log(Level.SEVERE, "Connection to ebs cannot be established.");
            throw new SiebelBusinessServiceException("NULL_DEF", "Connection to ebs cannot be established.");
        }
        if(e == null)
        {
            e = new EBSSql(EBS_CONN);
        }
        generateSalesOrder(salesOrder);
        generateOrderReservation();
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
        return DataConverter.toInt(orderNumber);
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
    
    private void generateSalesOrder(SalesOrderInventory salesOrder) throws SiebelBusinessServiceException
    {
        try 
        {
            List<SalesOrderInventory> list = new ArrayList();
            list.add(salesOrder);
            MyLogging.log(Level.INFO, "Describe Sales Order Inventory Object \n" + list);
            e.createSalesOrder(SIEBEL_CONN, salesOrder);
            returnValue = e.getString(15);
            Array arr = e.getArray(18);
            statusCode = e.getString(19);
            orderNumber = (e.getString(20) == null) ? "0" : e.getString(20).replace(" ", "");
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
            }
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "Caught SQL Exception:" + ERROR.toString());
            throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", ex.getMessage()); 
        }
        catch (NullPointerException ex)
        {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "Caught Null Pointer Exception:" + ERROR.toString());
            throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", ex.getMessage()); 
        }
    }
    
    private void generateOrderReservation() throws SiebelBusinessServiceException
    {
        if(returnValue.equalsIgnoreCase("s") && statusCode.equalsIgnoreCase("booked"))
        {
            try 
            {
                e.createOrderReservation(DataConverter.toInt(orderNumber));
                String r_id = e.getString(2);
                String r = e.getString(3);
                Array arr = e.getArray(4);
                String[] data = new String[]{};
                if (arr != null) 
                {
                   data = (String[]) arr.getArray();
                }
                MyLogging.log(Level.INFO, r + " " + r_id);
                MyLogging.log(Level.INFO, "----------------------------Return Reserved message--------------------------");
                for (String data1 : data) 
                {
                    hList.add(data1);
                    MyLogging.log(Level.INFO, data1);
                }
            } 
            catch (SQLException ex) 
            {
                ex.printStackTrace(new PrintWriter(ERROR));
                MyLogging.log(Level.SEVERE, "Caught Null Pointer Exception:" + ERROR.toString());
                throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", ex.getMessage()); 
            }
        }
        else
        {
            MyLogging.log(Level.SEVERE, "Cannot reserve order.");
        }
    }
    
    public void cancelOrder(Connection ebsConn, Integer order_number) throws SiebelBusinessServiceException, SQLException{
        EBSSqlData ebsData = new EBSSqlData(ebsConn);
        String ret = ebsData.getHeaderId(order_number);
        MyLogging.log(Level.SEVERE, "Order Number:" + ret);
        if(ret.length() == 0){
            throw new SiebelBusinessServiceException("NUM_EXCEPT", "Invalid order number. Please check your order number and try again.");
        }
        e = new EBSSql(ebsConn);
        e.cancelSalesOrder(ret);
        returnValue = e.getString(4);
        this.statusCode = e.getString(7);
        Array arr = e.getArray(8);
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
        }
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
        e = new EBSSql(ebsConn);
        e.cancelSalesLineOrder(header, line_id, quantity_ordered);
        this.returnValue = e.getString(6);
        //this.statusCode = e.getString(7);
        Array arr = e.getArray(9);
        this.line_order_status_code = new EBSSqlData(ebsConn).getOrderBookingStatus(
            "FLOW_STATUS_CODE", 
            "OE_ORDER_LINES_ALL", 
            "LINE_ID", 
            line_id
        );
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
