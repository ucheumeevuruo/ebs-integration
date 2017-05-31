/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.order;

import com.plexadasi.build.EBSSql;
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
    private static Connection EBS_CONN = null;
    private static SiebelDataBean SIEBEL_CONN = new SiebelDataBean();
    private static final StringWriter ERROR = new StringWriter();
    private Integer integerOutput;
    private String stringOutput;
    private final List<String> hList = new ArrayList();
    private String statusCode;
    private String orderNumber;
    private EBSSql e;
    
    public void doInvoke(PurchaseOrderInventory poInventory) throws SiebelBusinessServiceException
    {
        SIEBEL_CONN = poInventory.getSiebelConnection();
        EBS_CONN = poInventory.getEbsConnection();
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
        e.createPurchaseOrder(poInventory);
    }
}
