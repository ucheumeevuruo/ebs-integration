/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.objects.Impl;

import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author SAP Training
 */
public abstract class Product 
{
    /**
     * 
     */
    protected static final String BLANK = "";
    
    protected static final String PRODUCT_TYPE = "Product Type";
    
    protected static final String PRODUCT_TYPE_CODE = "Product Type Code";
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
    
    protected StringWriter error = new StringWriter();
    
    /**
     * 
     */
    protected SiebelPropertySet set = new SiebelPropertySet();
    
    /**
     * 
     */
    protected static SiebelDataBean CONN;
    
    protected static List<Map<String, String>> setList = new ArrayList();
    
    /**
     * 
     * @param connect 
     */
    public Product(SiebelDataBean connect)
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
    
    public List<Map<String, String>> getList()
    {
        return setList;
    }

    abstract public void doTrigger() throws SiebelBusinessServiceException;
}
