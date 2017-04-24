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
public class Order extends Product implements Impl
{
    public Order(SiebelDataBean CONN)
    {
        super(CONN);
    }
    
    /**
     * 
     */
    private static final String PLX_PRODUCT = "Product";
    
    /**
     * 
     */
    private static final String PLX_QUANTITY = "Quantity";
    
    /**
     * 
     */
    private static final String PLX_ITEM_PRICE = "Item Price";
    
    /**
     * 
     */
    private static final String PLX_INVENTORY = "Product Inventory Item Id";
    /**
     * 
     */
    private static final String BUS_OBJ = "Order";
    
    /**
     * 
     */
    private static final String BUS_COMP = "Order Item";
    
    
    
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
            
            s.setSField(set);
            
            setList = s.getSField(BUS_OBJ, BUS_COMP, this);
            
            MyLogging.log(Level.INFO, "Creating Objects: " + setList.get(0));
            
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
        sbBC.setSearchSpec("Quote Id", this.siebelAccountId);  
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {}
}