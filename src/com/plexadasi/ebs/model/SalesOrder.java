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
public class SalesOrder {
    private Integer quantity;
    private String orderNumber;
    private Integer onhandQuantity;
    private String partNumber;
    private String partName;
    private Integer warehouseId;

    /**
     * @return the partNumber
     */
    public String getPartNumber() {
        return partNumber;
    }

    /**
     * @return the warehouseId
     */
    public Integer getWarehouseId() {
        return warehouseId;
    }

    /**
     * @param partNumber the partNumber to set
     */
    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    /**
     * @param warehouseId the warehouseId to set
     */
    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @return the orderNumber
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * @return the onhandQuantity
     */
    public Integer getOnhandQuantity() {
        return onhandQuantity;
    }

    /**
     * @return the partName
     */
    public String getPartName() {
        return partName;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @param orderNumber the orderNumber to set
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * @param onhandQuantity the onhandQuantity to set
     */
    public void setOnhandQuantity(Integer onhandQuantity) {
        this.onhandQuantity = onhandQuantity;
    }

    /**
     * @param partName the partName to set
     */
    public void setPartName(String partName) {
        this.partName = partName;
    }
    
    @Override
    public String toString()
    {
        String NEW_LINE = System.getProperty("line.separator");
        String print = "Number:"+ this.orderNumber +NEW_LINE;
        print += "OHQ:"+ this.onhandQuantity +NEW_LINE;
        print += "Part Number:"+ this.partNumber +NEW_LINE;
        print += "Part Name:"+ this.partName +NEW_LINE;
        print += "Warehouse:"+this.warehouseId+NEW_LINE;
        print += "Onhand Quantity:"+ this.quantity +NEW_LINE;
        return print;
    }
}