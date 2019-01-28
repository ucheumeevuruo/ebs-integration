/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.invoice;

public class InvoiceObject {
    private String id;
    private String quoteNumber;
    private String v_customer_id;
    private String cust_trx_type_id;
    private String v_date;
    private String v_pri_salesrepid;
    private String v_curr;
    private String v_termId;
    private String v_siebelquote;
    private String v_ref;
    private Float localDeliveryCharges;
    private Float freight;
    private Float computerProgramming;
    private Integer warehouseId;
    private Float sundries;
    private Float withholdingTax;

    public String toString() {
        String NEW_LINE = System.getProperty("line.separator");
        String print = "Id:" + this.id + NEW_LINE;
        print = print + "Quote Number:" + this.quoteNumber + NEW_LINE;
        print = print + "Customer Id:" + this.v_customer_id + NEW_LINE;
        print = print + "Customer Transaction Id:" + this.cust_trx_type_id + NEW_LINE;
        print = print + "Transaction Date:" + this.v_date + NEW_LINE;
        print = print + "Sales Person Id:" + this.v_pri_salesrepid + NEW_LINE;
        print = print + "Currency:" + this.v_curr + NEW_LINE;
        print = print + "Term Id:" + this.v_termId + NEW_LINE;
        print = print + "Siebel Quote:" + this.v_siebelquote + NEW_LINE;
        print = print + "Reference Number:" + this.v_ref + NEW_LINE;
        print = print + "Local Delivery Charges:" + this.localDeliveryCharges + NEW_LINE;
        print = print + "Freight:" + this.freight + NEW_LINE;
        print = print + "Warehouse:" + this.warehouseId + NEW_LINE;
        print = print + "Sundries:" + this.sundries + NEW_LINE;
        print = print + "Computer Programming:" + this.computerProgramming + NEW_LINE;
        print = print + "Withholding tax:" + this.withholdingTax + NEW_LINE;
        return print;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBillToId(String value) {
        this.v_customer_id = value;
    }

    public void setCustomerTrxTypeId(String value) {
        this.cust_trx_type_id = value;
    }

    public void setTrxDate(String value) {
        this.v_date = value;
    }

    public void setPrimarySalesId(String value) {
        this.v_pri_salesrepid = value;
    }

    public void setTrxCurrency(String value) {
        this.v_curr = value;
    }

    public void setTermId(String value) {
        this.v_termId = value;
    }

    public void setCtRef(String value) {
        this.v_siebelquote = value;
    }

    public void setTrxDistId(String value) {
        this.v_siebelquote = value;
    }

    public String getId() {
        return this.id;
    }

    public String getBillToId() {
        return this.v_customer_id;
    }

    public String getCustomerTrxTypeId() {
        return this.cust_trx_type_id;
    }

    public String getTrxDate() {
        return this.v_date;
    }

    public String getPrimarySalesId() {
        return this.v_pri_salesrepid;
    }

    public String getTrxCurrency() {
        return this.v_curr;
    }

    public String getTermId() {
        return this.v_termId;
    }

    public String getCtRef() {
        return this.v_siebelquote;
    }

    public String getV_ref() {
        return this.v_ref;
    }

    public void setV_ref(String v_ref) {
        this.v_ref = v_ref;
    }

    public Float getLocalDeliveryCharges() {
        return this.localDeliveryCharges;
    }

    public void setLocalDeliveryCharges(Float localDeliveryCharges) {
        this.localDeliveryCharges = localDeliveryCharges;
    }

    public Float getFreight() {
        return this.freight;
    }

    public void setFreight(Float freight) {
        this.freight = freight;
    }

    public Float getComputerProgramming() {
        return this.computerProgramming;
    }

    public void setComputerProgramming(Float computerProgramming) {
        this.computerProgramming = computerProgramming;
    }

    public String getQuoteNumber() {
        return this.quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public Integer getWarehouseId() {
        return this.warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Float getSundries() {
        return this.sundries;
    }

    public void setSundries(Float sundries) {
        this.sundries = sundries;
    }

    public Float getWithholdingTax() {
        return this.withholdingTax;
    }

    public void setWithholdingTax(Float withholdingTax) {
        this.withholdingTax = withholdingTax;
    }
}

