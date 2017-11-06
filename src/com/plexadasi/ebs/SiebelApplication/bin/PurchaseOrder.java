/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.SiebelService;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.util.logging.Level;

/**
 *
 * @author SAP Training
 */
public class PurchaseOrder extends Product implements Impl
{
    public PurchaseOrder(SiebelDataBean CONN)
    {
        super(CONN);
    }
    
    /**
     * 
     */
    private static final String BUS_OBJ = "Order Entry";
    /**
     * 
     */
    private static final String BUS_COMP = "Order Entry - Line Items";
    
    /**
     * 
     * @throws com.siebel.eai.SiebelBusinessServiceException
     */
    @Override
    public void doTrigger() throws SiebelBusinessServiceException
    {
        try {
            SiebelService s = new SiebelService(CONN);
            
            set.setProperty(FIELD_LINE_NUMBER, FIELD_LINE_NUMBER);
            
            set.setProperty(SHIPMENT_NUMBER, SHIPMENT_NUMBER);
            
            //set.setProperty(PLX_LINE_TYPE, PLX_LINE_TYPE);
            
            set.setProperty(PLX_LOT_ID, PLX_LOT_ID);
            
            set.setProperty(PLX_PART_NUMBER, PLX_PART_NUMBER);
            
            set.setProperty(PLX_UNIT_OF_MEASURE, PLX_UNIT_OF_MEASURE);
            
            set.setProperty(FIELD_QUANTITY, FIELD_QUANTITY);
            
            set.setProperty(PLX_UNIT_PRICE, PLX_UNIT_PRICE);
            
            s.setSField(set);
            
            setList = s.getSField(BUS_OBJ, BUS_COMP, this);
            
            MyLogging.log(Level.INFO, "Creating Objects: " + setList);
            
            //MyLogging.log(Level.INFO, set.toString());
        } catch (SiebelException ex) {
            ex.printStackTrace(new PrintWriter(error));
            MyLogging.log(Level.SEVERE, "Caught Siebel Exception Line in doTrigger: " + error.toString());
            throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", error.toString()); 
        }
    }
    
    /**
     * 
     * @param sbBC
     * @throws SiebelException 
     */
    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException 
    {
        MyLogging.log(Level.INFO, siebelAccountId);
        sbBC.setSearchSpec("Order Number", this.siebelAccountId);  
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {}
}