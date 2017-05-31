/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.SiebelSearch;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Account;
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
public class PurchaseOrderOrganization extends Account implements Impl
{
    public PurchaseOrderOrganization(SiebelDataBean CONN)
    {
        super(CONN);
    }
    
    /**
     * 
     */
    private static final String BUS_COMP = "Account";
    
    /**
     * 
     */
    private static final String BUS_OBJ = "Account";
    
    
    /**
     * 
     * @throws com.siebel.eai.SiebelBusinessServiceException
     */
    @Override
    public void doTrigger() throws SiebelBusinessServiceException
    {
        SiebelSearch s = new SiebelSearch(CONN);
        
        s.setSField(ACC_CURRENCY_CODE, BLANK);
        
        s.setSField(ACC_PRI_SHIP_TO_ADDR, BLANK);
        
        s.setSField(ACC_PRI_BILL_TO_ADDR, BLANK);
        
        s.setSField(ACC_ORG_ID, BLANK);
        
        set = s.getSField(BUS_OBJ, BUS_COMP, this);
        
        MyLogging.log(Level.INFO, set.toString());
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
    public void getExtraParam(SiebelBusComp sbBC) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}