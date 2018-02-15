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
    public static final String PLX_PRODUCT = "Product";
    
    /**
     * 
     */
    public static final String PLX_PART_NUMBER = "Part Number";
    /**
     * 
     */
    public static final String FIELD_QUANTITY = "Quantity";
    
    /**
     * 
     */
    public static final String FIELD_LINE_NUMBER = "Line Number";
    /**
     * 
     */
    public static final String SHIPMENT_NUMBER = "Shipment Number";
    /**
     * 
     */
    public static final String FIELD_FREIGHT = "Freight Total";
    /**
     * 
     */
    public static final String PLX_TAX_RATE = "PLX Calculate VAT";
    /**
     * 
     */
    public static final String PLX_VAT = "PLX Vat";
    /**
     * 
     */
    public static final String PLX_LINE_TYPE = "";
    /**
     * 
     */
    public static final String PLX_LOT_ID  = "PLX Lot#";
    /**
     * 
     *
     */
    public static final String LOT_ID = "Lot#";
    /**
     * 
     */
    public static final String PLX_UNIT_OF_MEASURE = "Unit of Measure";
    /**
     * 
     */
    public static final String PLX_UNIT_PRICE = "Unit Price";
    /**
     * 
     */
    public static final String PLX_NET_PRICE = "Net Price";
    /**
     * 
     */
    public static final String PLX_DUE_DATE = "Requested Ship Date";
    /**
     * 
     */
    public static final String PLX_SHIP_TO_ORG_CODE = "Ship To Account Integration Id";
    /**
     * 
     */
    public static final String PLX_SHIP_TO_ORG_ID = "Ship To Account Id";
    /**
     * 
     */
    public static final String PLX_SHIP_TO_LOCATION_ID = "Ship To Address Id";
    /**
     * 
     */
    public static final String PLX_SHIP_TO_LOCATION = "Ship To Address";
    /**
     * 
     */
    public static final String PLX_ITEM_PRICE = "Item Price";
    /**
     * 
     */
    public static final String PLX_DISCOUNT_AMOUNT = "Discount Amount";
    /**
     * 
     */
    public static final String PLX_INVENTORY = "Product Inventory Item Id";
    /**
     * 
     */
    public static final String PLX_QUANTITY_AVAILABLE = "Available Quantity";
    /**
     * 
     */
    public static final String PLX_ORG_ID = "Organization Id";
    /**
     * 
     */
    public static final String PLX_QUANTITY_REQUESTED = "Quantity Requested";
    /**
     * 
     */
    public static final String PLX_PRIMARY_SALES_REP_ID = "PLX Primary Sales Rep Id";
    /**
     * 
     */
    public static final String PLX_CURRENCY_CODE = "Currency Code";
    /**
     * 
     */
    public static final String PLX_WAREHOUSE_ID = "PLX Warehouse Id";
    /**
     * 
     */
    public static final String PLX_AGENT_ID = "PLX Agent Id";
    /**
     * 
     */
    public static final String FIELD_ORDER_NUMBER = "Order Number";    
    /**
     * 
     */
    public static final String FIELD_LDC = "PLX Local Delivery Charges";
    
    public static final String PICK_MEANING = "PLX Pick Meaning";
    
    public static final String STATUS = "Status";
    public static final String RELEASE_STATUS = "PLX Release Status";
    protected String busComp;
    protected String busObj;
    
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
    
    protected SiebelPropertySet properties;
    
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
    public SiebelPropertySet getProperties()
    {
        return this.properties;
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
