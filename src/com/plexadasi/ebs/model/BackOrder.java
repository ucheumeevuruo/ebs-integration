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
public class BackOrder {
    private String pickMeaning;
    private String itemStatus;
    private String releaseStatus;
    private Integer quantity;

    /**
     * @return the pickMeaning
     */
    public String getPickMeaning() {
        return pickMeaning;
    }

    /**
     * @param pickMeaning the pickMeaning to set
     */
    public void setPickMeaning(String pickMeaning) {
        this.pickMeaning = pickMeaning;
    }

    /**
     * @return the itemStatus
     */
    public String getItemStatus() {
        return itemStatus;
    }

    /**
     * @param itemStatus the itemStatus to set
     */
    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    /**
     * @return the releaseStatus
     */
    public String getReleaseStatus() {
        return releaseStatus;
    }

    /**
     * @param releaseStatus the releaseStatus to set
     */
    public void setReleaseStatus(String releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    @Override
    public String toString()
    {
        String NEW_LINE = System.getProperty("line.separator");
        String print = "Pick Meaning:"+ this.pickMeaning +NEW_LINE;
        print += "Item Status:"+ this.itemStatus +NEW_LINE;
        print += "Release Status:"+ this.releaseStatus +NEW_LINE;
        print += "Quantity:"+ this.quantity +NEW_LINE;
        return print;
    }
}
