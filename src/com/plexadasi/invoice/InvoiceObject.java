/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.invoice;

/**
 *
 * @author SAP Training
 */
public class InvoiceObject {
    private String v_customer_id;
    private String cust_trx_type_id; 
    private String v_date;       
    private String v_pri_salesrepid;
    private String v_curr;
    private String v_termId;
    private String v_siebelquote;
    
    /**
     * 
     * @param value 
     */
    
    public void setBillToId(String value)
    {
        this.v_customer_id = value;
    }
    
    public void setCustomerTrxTypeId(String value)
    {
        this.cust_trx_type_id = value;
    }
    
    public void setTrxDate(String value)
    {
        this.v_date = value;
    }
    
    public void setPrimarySalesId(String value)
    {
        this.v_pri_salesrepid = value;
    }
    
    public void setTrxCurrency(String value)
    {
        this.v_curr = value;
    }
    
    public void setTermId(String value)
    {
        this.v_termId = value;
    }
    
    public void setCtRef(String value)
    {
        this.v_siebelquote = value;
    }
    
    public void setTrxDistId(String value)
    {
        this.v_siebelquote = value;
    }
    
    public String getBillToId()
    {
        return this.v_customer_id;
    }
    
    public String getCustomerTrxTypeId()
    {
        return this.cust_trx_type_id;
    }
    
    public String getTrxDate()
    {
        return this.v_date;
    }
    
    public String getPrimarySalesId()
    {
        return this.v_pri_salesrepid;
    }
    
    public String getTrxCurrency()
    {
        return this.v_curr;
    }
    
    public String getTermId()
    {
        return this.v_termId;
    }
    
    public String getCtRef()
    {
        return this.v_siebelquote;
    }
}
