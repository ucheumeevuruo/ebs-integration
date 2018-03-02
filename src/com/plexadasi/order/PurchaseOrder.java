/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.order;

import com.plexadasi.build.EBSSql;
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.siebel.data.SiebelDataBean;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author SAP Training
 */
public class PurchaseOrder {
    private Connection EBS_CONN = null;
    private SiebelDataBean SIEBEL_CONN = new SiebelDataBean();
    private static final StringWriter ERROR = new StringWriter();
    private Integer integerOutput;
    private String stringOutput;
    private final List<String> hList = new ArrayList();
    private String statusCode;
    private String orderNumber;
    private EBSSql e = null;
    
    public void doInvoke(PurchaseOrderInventory poInventory, SiebelDataBean sb, Connection ebs) throws SiebelBusinessServiceException
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
        poInventory.triggers(SIEBEL_CONN, new EBSSqlData(EBS_CONN));
        Boolean IsProcessed = this.processIsNotNull(EBS_CONN, poInventory.getOrderNumber());
        if(IsProcessed) {
        } else {
            e.createPurchaseOrder(SIEBEL_CONN, poInventory);
        }
    }
    
    private Boolean isPurchaseOrderProcessed(Connection ebsConn, String order_num) throws SiebelBusinessServiceException
    {
        String stat = new EBSSqlData(ebsConn).getOrderBookingStatus(
            "PROCESS_CODE", 
            "PO_HEADERS_INTERFACE", 
            "REFERENCE_NUM", 
            order_num
        );
        return (!stat.equalsIgnoreCase(""));
    }
    
    private Boolean processIsNotNull(Connection ebsConn, String order_num) throws SiebelBusinessServiceException
    {
        String stat = new EBSSqlData(ebsConn).getOrderBookingStatus(
            "PROCESS_CODE", 
            "PO_HEADERS_INTERFACE", 
            "REFERENCE_NUM", 
            order_num
        );
        return (!stat.equalsIgnoreCase(""));
    }
    
    public String getPONumber(Connection ebsConn, String order_num) throws SiebelBusinessServiceException
    {
        return new EBSSqlData(ebsConn).getOrderBookingStatus(
            "SEGMENT1", 
            "PO_HEADERS_ALL", 
            "PO_HEADER_ID", 
            getPurchaseOrderNumber(ebsConn, order_num)
        );
    }
    
    public String getPurchaseOrderNumber(Connection ebsConn, String order_num) throws SiebelBusinessServiceException
    {
        return new EBSSqlData(ebsConn).getOrderBookingStatus(
            "PO_HEADER_ID", 
            "PO_HEADERS_INTERFACE", 
            "REFERENCE_NUM", 
            order_num
        );
    }
    
    public String getPurchaseOrderBookingStatus(Connection ebsConn, String order_num) throws SiebelBusinessServiceException
    {
        if(isPurchaseOrderProcessed(ebsConn, order_num))
        {
            return new EBSSqlData(ebsConn).getOrderBookingStatus(
                "AUTHORIZATION_STATUS", 
                "PO_HEADERS_ALL", 
                "PO_HEADER_ID", 
                getPurchaseOrderNumber(ebsConn, order_num)
            );
        }
        return "";
    }
}
