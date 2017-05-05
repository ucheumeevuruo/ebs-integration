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
public class QLubricant extends Product implements Impl
{
    public QLubricant(SiebelDataBean CONN)
    {
        super(CONN);
    }
    
    /**
     * 
     */
    public static final String PLX_PRODUCT = "Product";
    
    /**
     * 
     */
    public static final String PLX_QUANTITY = "Quantity";
    
    /**
     * 
     */
    public static final String PLX_ITEM_PRICE = "Item Price";
    
    /**
     * 
     */
    public static final String PLX_INVENTORY = "Product Inventory Item Id";
    /**
     * 
     */
    public static final String PLX_LOT_ID  = "Lot#";
    /**
     * 
     */
    private static final String BUS_OBJ = "Quote";
    
    /**
     * 
     */
    private static final String BUS_COMP = "Quote Item";
    
    
    
    /**
     * 
     * @throws com.siebel.eai.SiebelBusinessServiceException
     */
    @Override
    public void doTrigger() throws SiebelBusinessServiceException
    {
        try {
            SiebelService s = new SiebelService(CONN);
            
            set.setProperty(PLX_PRODUCT, PLX_PRODUCT);
            
            set.setProperty(PLX_QUANTITY, PLX_QUANTITY);
            
            set.setProperty(PLX_INVENTORY, PLX_INVENTORY);
            
            set.setProperty(PLX_ITEM_PRICE, PLX_ITEM_PRICE);
            
            set.setProperty(PLX_LOT_ID, PLX_LOT_ID);
            
            s.setSField(set);
            
            setList = s.getSField(BUS_OBJ, BUS_COMP, this);
            
            MyLogging.log(Level.INFO, "Creating Objects: " + setList.toString());
            
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
        MyLogging.log(Level.INFO, this.siebelAccountId);
        sbBC.setSearchSpec("Quote Id", this.siebelAccountId);   
        sbBC.setSearchSpec(PRODUCT_TYPE, "Lubricants");  
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {}
}