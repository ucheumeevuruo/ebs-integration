/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.objects.Impl;

import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;

/**
 *
 * @author SAP Training
 */
public abstract class Account 
{
    
    /**
     * 
     */
    public static final String ACC_COUNTRY_CODE_FIELD = "PLX Country Code";
    
    /**
     * 
     */
    public static final String ACC_ADDRESS_FIELD = "Street Address";
    /**
     * 
     */
    public static final String ACC_CURRENCY_CODE = "Currency Code";
    /**
     * 
     */
    public static final String ACC_PRI_SHIP_TO_ADDR = "Primary Ship To Street Address";
    /**
     * 
     */
    public static final String ACC_PRI_BILL_TO_ADDR = "Primary Bill To Street Address";
    /**
     * 
     */
    public static final String ACC_POSTAL_FIELD = "Postal Code";
    /**
     * 
     */
    public static final String ACC_CITY_FIELD = "City";
    /**
     * 
     */
    public static final String ACC_ORG_ID = "Org Id";
    /**
     * 
     */
    public static final String ACC_STATE_FIELD = "State";
    /**
     * 
     */
    protected static final String BLANK = "";
    
    /**
     * 
     */
    protected String siebelAccountId;
    
    /**
     * 
     */
    protected String ebsAccountId;
    
    /**
     * 
     */
    protected String country_code;
    
    /**
     * 
     */
    protected String address;
    
    /**
     * 
     */
    protected String city;
    
    /**
     * 
     */
    protected String postal;
    
    /**
     * 
     */
    protected String state;
    
    /**
     * 
     */
    protected String module;
    
    /**
     * 
     */
    protected SiebelPropertySet set = new SiebelPropertySet();
    
    /**
     * 
     */
    protected static SiebelDataBean CONN;
    
    /**
     * 
     * @param connect 
     */
    public Account(SiebelDataBean connect)
    {
        CONN = connect;
    }
    
    /**
     * 
     * @param value 
     */
    public void setSiebelAccountId(String value)
    {
        this.siebelAccountId = value;
    }
    
    /**
     * 
     * @param value 
     */
    public void setEbsAccountId(String value)
    {
        this.ebsAccountId = value;
    }
    
    /**
     * 
     * @return 
     */
    public String getSiebelAccountId()
    {
        return this.siebelAccountId;
    }
    
    /**
     * 
     * @return 
     */
    public String getEbsAccountId()
    {
        return this.ebsAccountId;
    }
    
    /**
     * 
     * @return 
     */
    public String getCountryCode()
    {
        return this.country_code;
    }
    
    /**
     * 
     * @return 
     */
    public String getAddress() 
    {
        return this.address;
    }
    
    /**
     * 
     * @return 
     */
    public String getCity()
    {
        return this.city;
    }
    
    /**
     * 
     * @return 
     */
    public String getPostal()
    {
        return this.postal;
    }
    
    /**
     * 
     * @return 
     */
    public String getState()
    {
        return this.state;
    }
    
    
    public String getModule() 
    {
        return this.module;
    }
    
    public String getProperty(String property)
    {
        return set.getProperty(property);
    }

    abstract public void doTrigger() throws SiebelBusinessServiceException;
}
