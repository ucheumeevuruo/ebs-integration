/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.util.logging.Level;

/**
 *
 * @author SAP Training
 */
public class QQuote extends Product implements Impl
{
    public QQuote(SiebelDataBean CONN)
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
        MyLogging.log(Level.INFO, "Quote id is:= " +this.siebelAccountId);
        Product parts = new QParts(CONN);
        parts.setSiebelAccountId(this.siebelAccountId);
        parts.doTrigger();
        setList = parts.getList();
        QLubricant lub = new QLubricant(CONN);
        lub.setSiebelAccountId(this.siebelAccountId);
        lub.doTrigger();
        setList.addAll(lub.getList());
        QExpenses exp = new QExpenses(CONN);
        exp.setSiebelAccountId(this.siebelAccountId);
        exp.doTrigger();
        setList.addAll(exp.getList());
        QServices serv = new QServices(CONN);
        serv.setSiebelAccountId(this.siebelAccountId);
        serv.doTrigger();
        setList.addAll(serv.getList());
        QVehicle veh = new QVehicle(CONN);
        veh.setSiebelAccountId(this.getSiebelAccountId());
        veh.doTrigger();
        setList.addAll(veh.getList());
        MyLogging.log(Level.INFO, "Creating Quote: " + setList.toString());
        
        //MyLogging.log(Level.INFO, set.toString());
    }
    
    /**
     * 
     * @param sbBC
     * @throws SiebelException 
     */
    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException{}

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {}
}