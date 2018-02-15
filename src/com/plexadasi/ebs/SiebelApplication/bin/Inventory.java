/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

/**
 *
 * @author SAP Training
 */
public class Inventory {
    private String part_number;
    private Integer org_id;
    private Integer quantity;
    private Float amount;
    private String line_type;
    
    /**
     * @return the part_number
     */
    public String getPart_number() {
        return part_number;
    }

    /**
     * @param part_number the part_number to set
     */
    public void setPart_number(String part_number) {
        this.part_number = part_number;
    }

    /**
     * @return the org_id
     */
    public Integer getOrg_id() {
        return org_id;
    }

    /**
     * @param org_id the org_id to set
     */
    public void setOrg_id(Integer org_id) {
        this.org_id = org_id;
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

    /**
     * @return the amount
     */
    public Float getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Float amount) {
        this.amount = amount;
    }

    /**
     * @return the line_type
     */
    public String getLine_type() {
        return line_type;
    }

    /**
     * @param line_type the line_type to set
     */
    public void setLine_type(String line_type) {
        this.line_type = line_type;
    }

    public int getKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
