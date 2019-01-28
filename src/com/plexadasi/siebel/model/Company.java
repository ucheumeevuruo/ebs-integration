/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  org.plexada.si.siebel.annotations.BusinessField
 *  org.plexada.si.siebel.annotations.Entity
 */
package com.plexadasi.siebel.model;

import org.plexada.si.siebel.annotations.BusinessField;
import org.plexada.si.siebel.annotations.Entity;

@Entity(businessComponent="Account", businessObject="Account")
public class Company {
    @BusinessField(value="EBS Id")
    private Integer ebsId;
    @BusinessField(value="Street Address")
    private String streetAddress;
    @BusinessField(value="Postal Code")
    private String postalCode;
    @BusinessField(value="City")
    private String city;
    @BusinessField(value="State")
    private String state;
    @BusinessField(value="PLX Country Code")
    private String countryCode;
    @BusinessField(value="Currency Code")
    private String currencyCode;
    @BusinessField(value="Org Id")
    private Integer organizationId;
    @BusinessField(value="Primary Bill To Street Address")
    private String billToAddress;
    @BusinessField(value="Primary Bill To State")
    private String billToState;
    @BusinessField(value="Primary Bill To City")
    private String billToCity;
    @BusinessField(value="Primary Bill To Country")
    private String billToCountry;
    @BusinessField(value="Primary Ship To Street Address")
    private String shipToAddress;
    @BusinessField(value="Primary Ship To State")
    private String shipToState;
    @BusinessField(value="Primary Ship To City")
    private String shipToCity;
    @BusinessField(value="Primary Ship To Country")
    private String shipToCountry;

    public Integer getEbsId() {
        return this.ebsId;
    }

    public void setEbsId(Integer ebsId) {
        this.ebsId = ebsId;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Integer getOrganizationId() {
        return this.organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getBillToAddress() {
        return this.billToAddress;
    }

    public void setBillToAddress(String billToAddress) {
        this.billToAddress = billToAddress;
    }

    public String getBillToState() {
        return this.billToState;
    }

    public void setBillToState(String billToState) {
        this.billToState = billToState;
    }

    public String getBillToCity() {
        return this.billToCity;
    }

    public void setBillToCity(String billToCity) {
        this.billToCity = billToCity;
    }

    public String getBillToCountry() {
        return this.billToCountry;
    }

    public void setBillToCountry(String billToCountry) {
        this.billToCountry = billToCountry;
    }

    public String getShipToAddress() {
        return this.shipToAddress;
    }

    public void setShipToAddress(String shipToAddress) {
        this.shipToAddress = shipToAddress;
    }

    public String getShipToState() {
        return this.shipToState;
    }

    public void setShipToState(String shipToState) {
        this.shipToState = shipToState;
    }

    public String getShipToCity() {
        return this.shipToCity;
    }

    public void setShipToCity(String shipToCity) {
        this.shipToCity = shipToCity;
    }

    public String getShipToCountry() {
        return this.shipToCountry;
    }

    public void setShipToCountry(String shipToCountry) {
        this.shipToCountry = shipToCountry;
    }
}

