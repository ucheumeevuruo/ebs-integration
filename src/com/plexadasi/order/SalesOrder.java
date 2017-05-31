/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.order;

import com.plexadasi.build.EBSSql;
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
    private static Connection EBS_CONN = null;
    private static SiebelDataBean SIEBEL_CONN = new SiebelDataBean();
    private static final StringWriter ERROR = new StringWriter();
    private Integer integerOutput;
    private String stringOutput;
    private final List<String> hList = new ArrayList();
    private String returnValue = "";
    private String statusCode;
    private String orderNumber;
    private static EBSSql e;
    
    public void doInvoke(SalesOrderInventory salesOrder) throws SiebelBusinessServiceException
    {
        SIEBEL_CONN = salesOrder.getSiebelConnection();
        EBS_CONN = salesOrder.getEbsConnection();
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
        e = new EBSSql(EBS_CONN);
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
    
    private void generateSalesOrder(SalesOrderInventory salesOrder) throws SiebelBusinessServiceException
    {
        try 
        {
            List<SalesOrderInventory> list = new ArrayList();
            list.add(salesOrder);
            MyLogging.log(Level.INFO, "Describe Sales Order Inventory Object \n" + list);
            e.createSalesOrder(salesOrder);
            returnValue = e.getString(15);
            Array arr = e.getArray(18);
            statusCode = e.getString(19);
            orderNumber = e.getString(20).replace(" ", "");
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
                    //hList.add(data1);
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
    }
}
