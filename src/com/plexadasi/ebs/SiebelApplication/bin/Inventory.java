/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

public class Inventory {
    private String part_number;
    private Integer org_id;
    private Integer quantity;
    private Float amount;
    private Integer order_number;
    private String cust_po_number;
    private String line_type;

    public String getPart_number() {
        return this.part_number;
    }

    public void setPart_number(String part_number) {
        this.part_number = part_number;
    }

    public Integer getOrg_id() {
        return this.org_id;
    }

    public void setOrg_id(Integer org_id) {
        this.org_id = org_id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getAmount() {
        return this.amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getLine_type() {
        return this.line_type;
    }

    public void setLine_type(String line_type) {
        this.line_type = line_type;
    }

    public int getKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String toString() {
        String output = "";
        output = output + "\t\t[Siebel part number=" + this.part_number + "]\n";
        output = output + "\t\t[Line type=" + this.line_type + "]\n";
        output = output + "\t\t[Amount=" + this.amount + "]\n";
        output = output + "\t\t[Ship to org id=" + this.org_id + "]\n";
        output = output + "\t\t[Quantity=" + this.quantity + "]\n";
        return this.getClass().getSimpleName() + "\n[Details\n\t[\n" + output + "\t]\n";
    }

    public Integer getOrder_number() {
        return this.order_number;
    }

    public void setOrder_number(Integer order_number) {
        this.order_number = order_number;
    }

    public String getCust_po_number() {
        return this.cust_po_number;
    }

    public void setCust_po_number(String cust_po_number) {
        this.cust_po_number = cust_po_number;
    }
}

