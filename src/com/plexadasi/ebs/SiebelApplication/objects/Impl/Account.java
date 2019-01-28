/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelPropertySet
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.ebs.SiebelApplication.objects.Impl;

import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;

public abstract class Account {
    public static final String ACC_COUNTRY_CODE_FIELD = "PLX Country Code";
    public static final String ACC_ADDRESS_FIELD = "Street Address";
    public static final String ACC_CURRENCY_CODE = "Currency Code";
    public static final String ACC_PRI_SHIP_TO_ADDR = "Primary Ship To Street Address";
    public static final String ACC_PRI_BILL_TO_ADDR = "Primary Bill To Street Address";
    public static final String ACC_POSTAL_FIELD = "Postal Code";
    public static final String ACC_CITY_FIELD = "City";
    public static final String ACC_ORG_ID = "Org Id";
    public static final String ACC_EBS_ID = "EBS Id";
    public static final String ACC_STATE_FIELD = "State";
    public static final String ACC_BILL_TO_PRI_COUNTRY_FIELD = "Primary Bill To Country";
    public static final String ACC_BILL_TO_PRI_STATE_FIELD = "Primary Bill To State";
    public static final String ACC_BILL_TO_PRI_CITY_FIELD = "Primary Bill To City";
    public static final String ACC_SHIP_TO_PRI_COUNTRY_FIELD = "Primary Ship To Country";
    public static final String ACC_SHIP_TO_PRI_STATE_FIELD = "Primary Ship To State";
    public static final String ACC_SHIP_TO_PRI_CITY_FIELD = "Primary Ship To City";
    protected static final String BLANK = "";
    protected String siebelAccountId;
    protected String ebsAccountId;
    protected String country_code;
    protected String address;
    protected String city;
    protected String postal;
    protected String state;
    protected String module;
    protected SiebelPropertySet set = new SiebelPropertySet();
    protected static SiebelDataBean CONN;

    public Account(SiebelDataBean connect) {
        CONN = connect;
    }

    public void setSiebelAccountId(String value) {
        this.siebelAccountId = value;
    }

    public void setEbsAccountId(String value) {
        this.ebsAccountId = value;
    }

    public String getSiebelAccountId() {
        return this.siebelAccountId;
    }

    public String getEbsAccountId() {
        return this.ebsAccountId;
    }

    public String getCountryCode() {
        return this.country_code;
    }

    public String getAddress() {
        return this.address;
    }

    public String getCity() {
        return this.city;
    }

    public String getPostal() {
        return this.postal;
    }

    public String getState() {
        return this.state;
    }

    public String getModule() {
        return this.module;
    }

    public String getProperty(String property) {
        return this.set.getProperty(property);
    }

    public abstract void doTrigger() throws SiebelBusinessServiceException;
}

