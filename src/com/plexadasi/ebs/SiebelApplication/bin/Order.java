/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.SiebelSearch;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.util.logging.Level;

/**
 *
 * @author SAP Training
 */
public class Order extends Product implements Impl
{
    private SiebelSearch siebelSearch = null;
    
    private SiebelPropertySet setProp;
    
    public Order(SiebelDataBean CONN)
    {
        super(CONN);
        siebelSearch = new SiebelSearch(CONN);
    }
    
    /**
     * 
     */
    private static final String BUS_OBJ = "Order Entry";
    
    /**
     * 
     */
    private static final String BUS_COMP = "Order Entry - Orders";
    
    public void setPropertySet(String key, String value)
    {
        siebelSearch.setSField(key, value);
    }
    /**
     * 
     * @throws com.siebel.eai.SiebelBusinessServiceException
     */
    @Override
    public void doTrigger() throws SiebelBusinessServiceException
    {
        setProp = siebelSearch.getSField(BUS_OBJ, BUS_COMP, this);

        MyLogging.log(Level.INFO, "Creating Objects: " + setProp);
    }
    
    public String getProperty(String property)
    {
        return setProp.getProperty(property);
    }
    
    /**
     * 
     * @param sbBC
     * @throws SiebelException 
     */
    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException 
    {
        sbBC.setSearchSpec("Id", this.siebelAccountId);  
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {}
}