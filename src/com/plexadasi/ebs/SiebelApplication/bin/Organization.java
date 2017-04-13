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
public class Organization extends Account implements Impl
{
    public Organization(SiebelDataBean CONN)
    {
        super(CONN);
    }
    /**
     * 
     */
    private static final String ACC_COUNTRY_CODE_FIELD = "PLX Country Code";
    
    /**
     * 
     */
    private static final String ACC_ADDRESS_FIELD = "Street Address";
    
    /**
     * 
     */
    private static final String ACC_POSTAL_FIELD = "Postal Code";
    
    /**
     * 
     */
    private static final String ACC_CITY_FIELD = "City";
    
    /**
     * 
     */
    private static final String ACC_STATE_FIELD = "State";
    
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
        
        s.setSField(ACC_COUNTRY_CODE_FIELD, BLANK);
        
        s.setSField(ACC_ADDRESS_FIELD, BLANK);
        
        s.setSField(ACC_CITY_FIELD, BLANK);
        
        s.setSField(ACC_POSTAL_FIELD, BLANK);
        
        s.setSField(ACC_STATE_FIELD, BLANK);
        
        set = s.getSField(BUS_OBJ, BUS_COMP, this);
        
        MyLogging.log(Level.INFO, set.toString());
        
        this.country_code = set.getProperty(ACC_COUNTRY_CODE_FIELD);
        
        this.address = set.getProperty(ACC_ADDRESS_FIELD);
        
        this.city = set.getProperty(ACC_CITY_FIELD);
        
        this.postal = set.getProperty(ACC_POSTAL_FIELD);
        
        this.state = set.getProperty(ACC_STATE_FIELD);
        
        this.module = "BO_API";
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