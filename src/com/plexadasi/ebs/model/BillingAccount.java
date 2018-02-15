/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.model;

/**    
 *
 * @author SAP Training
 */
public class BillingAccount {
    private int id;
    private Integer siteUseId;
    private String billingType;
    private String address;
    /*
    public BillingAccount()
    {
        this.siteUseId = 0;
        this.billingType = "";
        this.id = 0;
        this.address = "";
    }
    */
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the siteUseId
     */
    public Integer getSiteUseId() {
        return siteUseId;
    }

    /**
     * @return the billingType
     */
    public String getBillingType() {
        return billingType;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param siteUseId the siteUseId to set
     */
    public void setSiteUseId(Integer siteUseId) {
        this.siteUseId = siteUseId;
    }

    /**
     * @param billingType the billingType to set
     */
    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
}
